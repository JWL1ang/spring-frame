/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:25:48
 */

package top.vjin.frame.core.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.lang.Nullable;
import top.vjin.frame.core.exception.FrameException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 资源工具类。
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Slf4j
public class ResourceUtils {
    @Getter(lazy = true)
    private static final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(ResourceUtils.class.getClassLoader());

    private ResourceUtils() {
    }

    /**
     * 根据通配符资源路径获取资源列表。
     *
     * @param wildcardResourcePaths 通配符资源路径
     * @return 返回资源列表。
     */
    public static Resource[] getResourcesByWildcard(String... wildcardResourcePaths) {
        ArrayList<Resource> resources = new ArrayList<>();

        for (String basename : wildcardResourcePaths) {
            try {
                resources.addAll(Arrays.asList(getResolver().getResources(basename)));
            } catch (IOException e) {
                log.warn("没有找到指定的资源路径：{}", basename);
            }
        }
        return resources.toArray(new Resource[0]);
    }

    /**
     * 根据资源路径获取资源。
     *
     * @param resourcePath 资源路径
     * @return 返回资源列表。
     */
    @Nullable
    public static Resource getResource(String resourcePath) {
        Resource[] resources = getResourcesByWildcard(resourcePath);
        return resources.length == 0 ? null : resources[0];
    }

    /**
     * 根据资源路径获取资源。
     *
     * @param resourcePath 资源路径
     * @return 返回资源列表。
     */
    public static InputStream getInputStream(String resourcePath) {
        try {
            Resource resource = getResource(resourcePath);
            if (resource == null) throw new FrameException("未找到资源:" + resourcePath);
            return resource.getInputStream();
        } catch (IOException e) {
            throw FrameException.of("获取资源getInputStream失败:" + e.getMessage(), e);
        }
    }
}
