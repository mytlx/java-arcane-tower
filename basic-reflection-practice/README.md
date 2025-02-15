# java 的基础反射练习
> 题目由 chatgpt 生成  
> 主要是一些基础的反射练习

### 1. **基础概念**
**题目**：通过反射，获取并打印出 `java.lang.String` 类的所有构造方法（包括公共、私有等）。

### 2. **获取类的信息**
**题目**：使用反射获取 `java.util.ArrayList` 类的类名，并打印它是否是 `List` 的子类。

### 3. **获取字段（Field）**
**题目**：创建一个简单的类 `Person`，包含 `private` 字段 `name` 和 `age`。使用反射获取并修改这两个字段的值，然后打印出修改后的值。

```java
public class Person {
    private String name;
    private int age;
}
```

### 4. **获取方法（Method）**
**题目**：在 `Person` 类中增加一个方法 `sayHello()`，使用反射调用该方法，并传入参数 `"John"`，让它打印出 `Hello, John!`。

```java
public class Person {
    private String name;
    private int age;

    public void sayHello(String name) {
        System.out.println("Hello, " + name + "!");
    }
}
```

### 5. **访问私有成员**
**题目**：通过反射访问一个类的私有方法，并调用它。你可以定义一个 `private` 方法，然后尝试通过反射来调用它。

```java
public class MyClass {
    private void secretMethod() {
        System.out.println("This is a secret method!");
    }
}
```

### 6. **反射调用构造方法（Constructor）**
**题目**：创建一个类 `Car`，包含带参数的构造函数（比如 `String model, int year`）。使用反射调用该构造函数并创建 `Car` 对象。

```java
public class Car {
    private String model;
    private int year;

    public Car(String model, int year) {
        this.model = model;
        this.year = year;
    }
}
```

### 7. **反射与继承**
**题目**：创建一个父类 `Animal` 和一个子类 `Dog`。使用反射获取父类 `Animal` 的方法，并尝试调用它。

```java
public class Animal {
    public void makeSound() {
        System.out.println("Animal sound");
    }
}

public class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Bark");
    }
}
```

### 8. **反射与数组**
**题目**：使用反射创建一个 `int[]` 数组并给它赋值。然后通过反射获取并打印出数组的元素。

### 9. **获取注解信息**
**题目**：创建一个类，并给它添加一个注解 `@Deprecated`，然后使用反射获取并打印该类是否被标注为 `@Deprecated`。

```java
@Deprecated
public class OldClass {
}
```

### 10. **反射与动态代理**
**题目**：创建一个接口 `Service`，以及它的实现类 `ServiceImpl`。使用 JDK 动态代理创建代理对象，并在调用方法时打印日志。

```java
public interface Service {
    void execute();
}

public class ServiceImpl implements Service {
    @Override
    public void execute() {
        System.out.println("Executing service...");
    }
}
```

### 11. **反射性能测试**
**题目**：用反射调用一个方法和直接调用这个方法，比较二者的执行时间差异，分析反射带来的性能损耗。

### 12. **反射应用：工厂模式**
**题目**：通过反射实现一个简单的工厂模式，根据传入的类名实例化对应的对象。例如，`Car` 或 `Bike`。

```java
public class Car {
    public void drive() {
        System.out.println("Driving a car!");
    }
}

public class Bike {
    public void ride() {
        System.out.println("Riding a bike!");
    }
}
```
