# Dynamic-Datasource

### 1. 使用AbstractRoutingDataSource，重写determineCurrentLookupKey；
### 2. 自定义注解VariableDataSource，通过参数决定使用哪个数据源；
### 3. 自定义AoP，拦截VariableDataSource，设置线程变量中数据源名称，线程结束时及时清除；

## 使用方法：
### 1. 执行resource/sql下的test.sql文件

### 2. 修改application.yml文件中关于datasource的配置

### 3. 使用localhost:8081/master和localhost:8081/readonly来看返回的数据是2还是1，确定动态数据源是否生效

## 改进：
### 1. 没有设置事务的相关处理

### 2. 对于SqlSessionFactory没有深入了解