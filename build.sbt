val Http4sVersion = "0.18.4"
val ScalatestVersion = "3.0.5"
val LogbackVersion = "1.2.3"
val MongoDriverVersion = "2.2.1"

lazy val root = (project in file("."))
  .settings(
    organization := "com.example",
    name := "offers-api",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.5",
    libraryDependencies ++= Seq(
      "org.http4s"        %% "http4s-blaze-server"  % Http4sVersion,
      "org.http4s"        %% "http4s-circe"         % Http4sVersion,
      "org.http4s"        %% "http4s-dsl"           % Http4sVersion,
      "org.scalatest"     %% "scalatest"            % ScalatestVersion % Test,
      "ch.qos.logback"    %  "logback-classic"      % LogbackVersion,
      "org.mongodb.scala" %% "mongo-scala-driver"   % MongoDriverVersion
    )
  )

