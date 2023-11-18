package com.ws;

import com.ws.base.model.BaseModel;
import com.ws.cache.BaseColumnType;
import com.ws.cache.ModelInfoCache;
import com.ws.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author GSF
 */
@Slf4j
public class GlobalParam {

    public static Class<?> mainClazz;

    public static ApplicationContext applicationContext;

    public static String contextPath;

    public static String getContextRunPath() {
        if (StringUtil.isEmpty(GlobalParam.contextPath)) {
            ApplicationHome applicationHome = new ApplicationHome();
            String path = applicationHome.getDir().getAbsolutePath();
            path = Paths.get(URLDecoder.decode(path, StandardCharsets.UTF_8)).normalize().toString();
            if (StringUtil.isEmpty(path)) {
                throw new RuntimeException("获取路径失败");
            }
            GlobalParam.contextPath = path;
        }
        return GlobalParam.contextPath;
    }

    public static @Nullable HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(servletRequestAttributes)) {
            return null;
        } else {
            return servletRequestAttributes.getRequest();
        }
    }

    public static @Nullable HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(servletRequestAttributes)) {
            return null;
        } else {
            return servletRequestAttributes.getResponse();
        }
    }

    public static HttpSession getSession() {
        return Objects.requireNonNull(getRequest()).getSession();
    }

    public static Set<Class<? extends BaseModel>> modelClazz = new HashSet<>();

    /**
     * <p>key是 Class.getName() 全类名</></p>
     **/
    public static Map<String, ModelInfoCache> modelInfoCacheMap = new HashMap<>();

    /**
     * <p>key是 Class.getName() 全类名</></p>
     **/
    public static Map<String, List<BaseColumnType>> modelColumnTypeCacheMap = new HashMap<>();

    /**
     * <p>key是 Class.getName() 全类名</></p>
     **/
    public static Map<String, Class<? extends BaseModel>> serviceModelGenericityCacheMap = new HashMap<>();

    /**
     * <p>key是 Class.getName() 全类名</></p>
     **/
    public static Map<String, Class<? extends BaseModel>> controllerModelGenericityCacheMap = new HashMap<>();

    public static String getUUID(boolean replace) {
        if (replace) {
            return UUID.randomUUID().toString().replaceAll("-", "");
        }
        return UUID.randomUUID().toString();
    }

    public static Set<Class<? extends BaseModel>> getCurrentPackageModelClazz(String packagePath, Predicate<Class<? extends BaseModel>> predicate) throws IOException, ClassNotFoundException {
        Set<Class<? extends BaseModel>> modelClazz = new HashSet<>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String pattern = StringUtil.concat(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(packagePath), "/**/*.class");
        Resource[] resources = resourcePatternResolver.getResources(pattern);
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        for (Resource resource : resources) {
            MetadataReader reader = metadataReaderFactory.getMetadataReader(resource);
            String className = reader.getClassMetadata().getClassName();
            Class<?> clazz = Class.forName(className);
            if (BaseModel.class.isAssignableFrom(clazz)) {
                if (predicate.test((Class<? extends BaseModel>) clazz)) {
                    modelClazz.add((Class<? extends BaseModel>) clazz);
                }
            }
        }
        return modelClazz;
    }

    public static Set<Class<? extends BaseModel>> getCurrentPackageModelClazz(String[] packages, Predicate<Class<? extends BaseModel>> predicate) throws IOException, ClassNotFoundException {
        Set<Class<? extends BaseModel>> modelClazz = new HashSet<>();
        for (String modelPackage : packages) {
            modelClazz.addAll(getCurrentPackageModelClazz(modelPackage, predicate));
        }
        return modelClazz;
    }

}
