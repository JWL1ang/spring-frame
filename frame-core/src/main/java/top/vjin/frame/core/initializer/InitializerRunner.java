/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:2:22
 */

package top.vjin.frame.core.initializer;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.vjin.frame.core.exception.FrameException;
import top.vjin.frame.core.utils.CollectionUtils;
import top.vjin.frame.core.utils.Watch;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 初始化运行器
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Slf4j
@Component
@Setter(onMethod = @__(@Autowired))
public class InitializerRunner implements CommandLineRunner {

    private List<Initializer> initializers;

    @Override
    public void run(String... args) throws Exception {
        Watch total = Watch.start();
        Map<Initializer, Set<Initializer>> initializerToDependents = getInitializerToDependents(initializers);
        List<List<Initializer>> orderedInitializers = getOrderedInitializers(initializerToDependents);

        for (List<Initializer> list : orderedInitializers) {
            list.parallelStream().forEach(it -> {
                Watch watch = Watch.start();
                it.init();
                log.info("{} 初始化完成，耗时{}秒", AopUtils.getTargetClass(it).getSimpleName(), watch.getSecond());
            });
        }
        log.info("初始化完成，耗时{}秒", total.getSecond());
    }

    /**
     * 获取初始化器及其依赖
     *
     * @param initializers 初始化器
     */
    private Map<Initializer, Set<Initializer>> getInitializerToDependents(List<Initializer> initializers) {
        //记录所有初始化器的class
        Map<Class, Initializer> classToInitializer = initializers.stream()
                .collect(Collectors.toMap(AopUtils::getTargetClass, it -> it, (o, n) -> o));

        //初始化器及推荐关系
        Map<Initializer, Set<Initializer>> initializerToDependents = new HashMap<>();

        //计算依赖关系
        for (Initializer it : initializers) {
            //需要依赖的
            Set<Initializer> dependents = initializerToDependents.computeIfAbsent(it, k -> new HashSet<>());
            dependents.addAll(it.getFronts().stream().map(classToInitializer::get).filter(dependent -> !Objects.equals(dependent, it)).filter(Objects::nonNull).collect(Collectors.toList()));

            //被依赖的初始化类列表
            for (Class<? extends Initializer> behindClass : it.getBehinds()) {
                Initializer behind = classToInitializer.get(behindClass);
                if (behind != null && !Objects.equals(behind, it)) {
                    //被依赖的初始化器如果存在,则找到它的依赖列表,然后把it(当前初始化器)加入到其中
                    initializerToDependents.computeIfAbsent(behind, k -> new LinkedHashSet<>()).add(it);
                }
            }
        }

        return initializerToDependents;
    }

    /**
     * 获取排序后的初始化器
     *
     * @param initializerToDependents 初始化器及其依赖
     */
    public List<List<Initializer>> getOrderedInitializers(Map<Initializer, Set<Initializer>> initializerToDependents) {
        //保存一份初始化器及其依赖，作为待处理的列表
        TreeMap<ComparableInitializer, TreeSet<ComparableInitializer>> unprocessed = getOrdered(initializerToDependents);

        //每一个初始化器的依赖
        Collection<TreeSet<ComparableInitializer>> eachDependents = unprocessed.values();

        //排好顺序的初始化器
        List<List<Initializer>> orderedInitializers = new LinkedList<>();

        //如果初始化器
        while (!unprocessed.isEmpty()) {
            //获取已经没有依赖的初始化器
            List<ComparableInitializer> noDependentsList = unprocessed.entrySet().stream()
                    .filter(it -> it.getValue().isEmpty())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            if (noDependentsList.isEmpty()) {
                //如果无法进行初始化
                Set<ComparableInitializer> dependents = new LinkedHashSet<>();

                ComparableInitializer temp = unprocessed.firstKey();
                while (dependents.add(temp)) {
                    temp = unprocessed.get(temp).first();
                }

                List<ComparableInitializer> msg = new LinkedList<>(dependents);
                Iterator<ComparableInitializer> iterator = msg.iterator();
                while (iterator.hasNext()) {
                    ComparableInitializer init = iterator.next();
                    if (Objects.equals(init, temp)) {
                        break;
                    } else {
                        iterator.remove();
                    }
                }
                msg.add(temp);

                String text = CollectionUtils.joinToString(msg, "->", "", "", it -> AopUtils.getTargetClass(it.getInitializer()).getSimpleName());
                throw FrameException.of("初始化器类循环依赖:" + text);
            }

            //加入到 按顺序排好的初始化器
            orderedInitializers.add(noDependentsList.stream().map(ComparableInitializer::getInitializer).collect(Collectors.toList()));
            //待处理列表中移除
            noDependentsList.forEach(unprocessed::remove);
            //在各自的依赖中集合删除已排好序的序列化器
            for (Set<ComparableInitializer> set : eachDependents) {
                noDependentsList.forEach(set::remove);
            }
        }
        return orderedInitializers;
    }


    /**
     * 获取排好序的初始化器及其依赖
     *
     * @param initializerToDependents 初始化器及其依赖
     * @return 排好序的初始化器及其依赖
     */
    private TreeMap<ComparableInitializer, TreeSet<ComparableInitializer>> getOrdered(Map<Initializer, Set<Initializer>> initializerToDependents) {
        //原序列化器与可排序的序列化器的关系
        Map<Initializer, ComparableInitializer> initializerToComparableInitializer = initializerToDependents.keySet()
                .stream()
                .collect(Collectors.toMap(it -> it, ComparableInitializer::new));

        TreeMap<ComparableInitializer, TreeSet<ComparableInitializer>> ordered = new TreeMap<>();
        for (Map.Entry<Initializer, Set<Initializer>> entry : initializerToDependents.entrySet()) {
            ComparableInitializer key = initializerToComparableInitializer.get(entry.getKey());
            TreeSet<ComparableInitializer> value = entry.getValue().stream().map(ComparableInitializer::new)
                    .collect(Collectors.toCollection(TreeSet::new));
            ordered.put(key, value);
        }
        return ordered;
    }


    /**
     * 可比较的初始化序列器
     */
    @Getter
    public static class ComparableInitializer implements Initializer, Comparable<ComparableInitializer> {

        @Delegate
        private Initializer initializer;
        private String targetClassName;

        private ComparableInitializer(Initializer initializer) {
            this.initializer = initializer;
            this.targetClassName = AopUtils.getTargetClass(initializer).getName();
        }

        @Override
        public int compareTo(@NotNull ComparableInitializer o) {
            return targetClassName.compareTo(o.targetClassName);
        }

        @Override
        public boolean equals(Object o) {
            return o == this
                    || (o instanceof ComparableInitializer && initializer.equals(((ComparableInitializer) o).initializer))
                    || initializer.equals(o);
        }

        @Override
        public int hashCode() {
            return initializer.hashCode();
        }
    }
}
