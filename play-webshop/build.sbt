name := "hibernate-test"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  //javaJdbc,
  //javaEbean,
  javaJpa,
  cache
)     

libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "4.2.8.Final"

libraryDependencies += "mysql" % "mysql-connector-java" % "+"

play.Project.playJavaSettings
