/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:23:4
 */

package top.vjin.frame.core.initializer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import top.vjin.frame.core.utils.ResourceUtils;
import top.vjin.frame.core.utils.Threads;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.UUID;

/**
 * 消息初始化器
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Slf4j
@Component
@AllArgsConstructor
public class MessageInitializer implements Initializer {

    private MessageSourceProperties messageSourceProperties;
    private HierarchicalMessageSource messageSource;

    @Override
    public void init() {
        ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
        messageBundle.setBasename("classpath*:/*messages");
        messageBundle.setResourceLoader(new WildcardResourceLoader());
        messageBundle.setDefaultEncoding(messageSourceProperties.getEncoding().name());

        messageSource.setParentMessageSource(messageBundle);

        //异步使配置生效
        Threads.start(() -> messageSource.getMessage(UUID.randomUUID().toString(), new Object[0], "", Locale.CHINESE));
    }


    /**
     * 通配符资源加载器
     */
    public static class WildcardResourceLoader implements ResourceLoader {

        @Override
        public Resource getResource(String location) {
            return new WildcardResource(location);
        }

        @Override
        public ClassLoader getClassLoader() {
            return ClassUtils.getDefaultClassLoader();
        }
    }

    /**
     * 通配符资源
     */
    public static class WildcardResource implements Resource {

        /** 资源 */
        private Resource[] resources;

        /** 资源通配符 */
        private String resourceWildcard;

        /** 数据 */
        private byte[] bytes;

        public WildcardResource(String resourceWildcard) {
            this.resourceWildcard = resourceWildcard;
            this.resources = ResourceUtils.getResourcesByWildcard(resourceWildcard);
        }

        private byte[] getBytes() throws IOException {
            if (bytes != null) return bytes;
            synchronized (this) {
                if (bytes != null) return bytes;

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                for (Resource resource : resources) {
                    byte[] byteArray = new byte[4096];
                    try (InputStream inputStream = resource.getInputStream()) {
                        int size;
                        while ((size = inputStream.read(byteArray)) > 0) {
                            outputStream.write(byteArray, 0, size);
                        }
                        outputStream.write("\r\n".getBytes());
                    }
                }
                bytes = outputStream.toByteArray();
                return bytes;
            }
        }

        @Override
        public boolean exists() {
            return resources.length != 0;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(getBytes());
        }

        @Override
        public String getFilename() {
            return resourceWildcard;
        }

        @Override
        public long contentLength() throws IOException {
            return getBytes().length;
        }

        @Override
        public long lastModified() throws IOException {
            return 0;
        }

        @Override
        public String getDescription() {
            return resourceWildcard;
        }

        @Override
        public File getFile() throws IOException {
            throw new UnsupportedOperationException("方法不受支持");
        }

        @Override
        public URL getURL() throws IOException {
            throw new UnsupportedOperationException("方法不受支持");
        }

        @Override
        public URI getURI() throws IOException {
            throw new UnsupportedOperationException("方法不受支持");
        }

        @Override
        public Resource createRelative(String relativePath) throws IOException {
            throw new UnsupportedOperationException("方法不受支持");
        }
    }
}
