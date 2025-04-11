# JDK 9 features

## 模块化系统（Jigsaw）

### **`module-info.java` 详解**
`module-info.java` 是 **JDK 9** 引入的 **Java 模块系统（JPMS, Java Platform Module System）** 的核心配置文件。它用于 **定义模块的依赖、暴露的 API 以及使用的服务**，从而提高项目的可维护性和安全性。

---

### **1. `module-info.java` 的基本语法**
```java
module <模块名> {
    requires <依赖模块>;       // 依赖其他模块
    exports <包名>;           // 导出包，供其他模块使用
    opens <包名>;             // 允许反射访问（用于 Spring、JPA 等）
    uses <接口名>;            // 声明使用的服务（SPI 机制）
    provides <接口> with <实现类>;  // 提供接口的实现
}
```

#### **示例**
```java
module com.mytlx.jdk.features.jdk9 {
    requires java.net.http;   // 依赖 JDK 的 HTTP 客户端模块
    requires java.sql;        // 依赖 Java SQL API 模块
    
    exports com.mytlx.jdk.features.jdk9.api;  // 导出 API 供其他模块使用
    opens com.mytlx.jdk.features.jdk9.internal;  // 允许其他模块通过反射访问该包
}
```
---

### **2. `module-info.java` 主要关键字**
#### **（1）`module`**
- **定义模块的名称**，必须唯一，通常采用 **逆向域名命名**（如 `com.example.myapp`）。
- 一个项目只能有 **一个 `module-info.java`**，必须位于 `src/main/java/模块名/` 目录下。

```java
module com.mytlx.jdk.features {
    // 模块定义
}
```

---

#### **（2）`requires`（声明依赖）**
- **指定当前模块需要的其他模块**。
- JDK 的核心 API（如 `java.sql`、`java.net.http`）也被拆分成多个模块，需要显式 `requires`。
- **示例：**
```java
module com.mytlx.jdk.features.jdk9 {
    requires java.sql;        // 依赖 Java SQL API
    requires java.net.http;   // 依赖 HTTP 客户端
}
```

- **模块化的 JDK 结构（部分）**

  | JDK 模块 | 主要 API |
  |----------|---------|
  | `java.base` | `java.lang`，`java.util`（默认引入，无需 `requires`） |
  | `java.sql` | JDBC |
  | `java.net.http` | `HttpClient` |
  | `java.logging` | `java.util.logging` |
  | `java.xml` | XML 解析 |

---

#### **（3）`exports`（导出包）**
- **将当前模块中的某些包暴露给其他模块**。
- 只有 `exports` 的包才能被其他模块 `import`。
- **示例：**
```java
module com.mytlx.jdk.features.jdk9 {
    exports com.mytlx.jdk.features.jdk9.api;  // 只暴露 API 包，内部包不暴露
}
```
- **如果不加 `exports`，其他模块无法访问该模块的任何类**，即使 `requires` 了这个模块。

---

#### **（4）`opens`（反射访问）**
- `opens` 允许 **其他模块使用反射（`setAccessible(true)`）访问私有成员**，用于框架（如 Spring, Hibernate）。
- `exports` 只是允许 **编译时访问**，但 `opens` 允许 **运行时反射访问**。
- **示例：**
```java
module com.mytlx.jdk.features.jdk9 {
    opens com.mytlx.jdk.features.jdk9.internal;  // 允许反射访问
}
```

---

#### **（5）`uses`（声明服务依赖）**
- **用于 Java 的 SPI（Service Provider Interface）机制**，表示当前模块**要使用某个接口的实现**。
- **示例：**
```java
module com.mytlx.jdk.features.jdk9 {
    uses java.sql.Driver;  // 使用 JDBC 驱动
}
```

---

#### **（6）`provides ... with ...`（提供服务实现）**
- **用于 Java SPI**，指定当前模块提供某个接口的实现。
- **示例：**
```java
module com.mytlx.jdk.features.jdk9 {
    provides java.sql.Driver with com.mytlx.jdbc.MySQLDriver;  
}
```
- 这样，JVM 在运行时会自动发现 `com.mytlx.jdbc.MySQLDriver` 并加载。

---

### **3. `module-info.java` 在 IDEA 中的使用**
#### **（1）检查 `Project Structure`**
1. **打开 IDEA**，按 `Ctrl + Alt + Shift + S` 进入 `Project Structure`。
2. 选择 `Modules` 选项卡：
    - 确保 `Language level` **≥ 9**。
    - `Dependencies` 里添加 `java.net.http` 等需要的模块。

#### **（2）IDEA 运行配置**
如果 `module-info.java` 报错：
1. 在 `Run/Debug Configurations` 里，找到 `JVM options` 选项。
2. **添加 `--add-modules` 参数（非模块化项目时需要）：**
   ```
   --add-modules java.net.http
   ```

---

### **4. `module-info.java` 与 `class-path`**
#### **（1）模块化项目使用 `--module-path`**
```sh
javac --module-path out -d out src/main/java/com/mytlx/jdk/features/jdk9/httpclient/HttpClientExample.java
java --module-path out -m com.mytlx.jdk.features/com.mytlx.jdk.features.jdk9.httpclient.HttpClientExample
```
> **注意：** `--module-path` 替代了传统的 `-cp`（`class-path`）。

---

#### **（2）非模块化项目（旧项目）使用 `--add-modules`**
如果你的项目没有 `module-info.java`，可以用 `--add-modules`：
```sh
java --add-modules java.net.http -cp out com.mytlx.jdk.features.jdk9.httpclient.HttpClientExample
```
> **适用于：** 迁移旧代码到 JDK 9+，但不想改 `module-info.java`。

---

## **总结**
| 关键字 | 作用 |
|--------|------|
| `module` | 定义模块 |
| `requires` | 声明依赖 |
| `exports` | 导出包，允许其他模块访问 |
| `opens` | 允许其他模块反射访问 |
| `uses` | 使用某个接口（SPI） |
| `provides ... with ...` | 提供某个接口的实现（SPI） |

---
