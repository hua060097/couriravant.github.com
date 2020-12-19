package com.example.routerlibrary;

import com.example.boundapi.AutoBundle;
import com.google.auto.service.AutoService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class AutoBoundProcessor extends AbstractProcessor {

    private Elements elementUtils;
    //编译阶段回调，
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        System.out.println("init: ");
    }

    private List<FieldHolder> fieldList = new ArrayList<>();
    private HashMap<String, List<FieldHolder>> map = new HashMap<>();

    //编译过程回调，获取注解，处理后生成代码
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set != null && !set.isEmpty()) {

            for (Element element: roundEnvironment.getElementsAnnotatedWith(AutoBundle.class)) {
                if (element.getKind() == ElementKind.FIELD) {
                    VariableElement variableElement = (VariableElement) element;
                    //typeElement中包含的数据包括类名、包名等。
                    TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
                    String className = typeElement.getSimpleName().toString();
                    String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();

                    AutoBundle autoBundle = variableElement.getAnnotation(AutoBundle.class);
                    boolean require = autoBundle.require();

                    Name name = variableElement.getSimpleName();
                    TypeMirror type = variableElement.asType();

                    FieldHolder fieldHolder = new FieldHolder();
                    fieldHolder.name = name.toString();
                    fieldHolder.type = type.toString();

                    List<FieldHolder> fieldHolders = map.get(className);
                    if (fieldHolders == null || fieldHolders.isEmpty()) {
                        fieldHolders = new ArrayList<>();
                        fieldHolders.add(fieldHolder);
                        map.put(className, fieldHolders);
                    } else {
                        fieldHolders.add(fieldHolder);
                    }

                    System.out.println("packageName: "+packageName + " className: "+className);
                    System.out.println("name: "+name);
                }
            }

            processAuto();


            return true;
        }

        return false;
    }

    //https://cloud.tencent.com/developer/article/1462889
    //https://github.com/yangchong211/YCAptHelper?spm=a2c4e.10696291.0.0.136119a49FqFlp
    //https://www.jianshu.com/p/b5be6b896a1a
    private void processAuto() {
        for(Map.Entry<String, List<FieldHolder>> entry : map.entrySet()) {
            List<FieldHolder> fieldHolderList = entry.getValue();
            for (FieldHolder fieldHolder: fieldHolderList) {

            }
        }
    }

    //需要处理哪些注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //创建一个set集合，set集合特点是无序不重复
        Set<String> types = new LinkedHashSet<>();
        //添加到集合中
        types.add(AutoBundle.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        //获取支持的版本号
        return SourceVersion.RELEASE_7;
    }
}
