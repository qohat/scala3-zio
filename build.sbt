import Dependencies._

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "3.1.0"
ThisBuild / version := "1.0.0"
ThisBuild / organization := "com.qohat"
ThisBuild / organizationName := "com.qohat"

ThisBuild / evictionErrorLevel := Level.Warn
ThisBuild / scalafixDependencies += Libraries.organizeImports

resolvers += Resolver.sonatypeRepo("snapshots")

val scalafixCommonSettings = inConfig(IntegrationTest)(scalafixConfigSettings(IntegrationTest))

lazy val root = (project in file("."))
  .settings(
    name := "scala3-zio"
  )
  .aggregate(core, tests)


lazy val tests = (project in file("modules/tests"))
  .configs(IntegrationTest)
  .settings(
    name := "scala3-zio-test-suite",
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
    Defaults.itSettings,
    scalafixCommonSettings,
    libraryDependencies ++= Seq(
      Libraries.catsEffect,
      Libraries.http4sDsl,
      Libraries.http4sBlaze,
      Libraries.http4sCirce,
      Libraries.circeCore,
      Libraries.circeGeneric,
      Libraries.circeParser,
      Libraries.log4cats,
      Libraries.logback % Runtime,
      Libraries.derevoCore,
      Libraries.derevoCats,
      Libraries.derevoCirce,
      Libraries.refinedCore,
      Libraries.refinedCats,
      Libraries.newtype
    )
  )
  .dependsOn(core)

lazy val core = (project in file("modules/core"))
  .enablePlugins(DockerPlugin)
  .enablePlugins(AshScriptPlugin)
  .settings(
    name := "scala3-zio-core",
    Docker / packageName := "scala3-zio",
    scalafmtOnCompile := true,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    Defaults.itSettings,
    scalafixCommonSettings,
    dockerBaseImage := "openjdk:11-jre-slim-buster",
    dockerExposedPorts ++= Seq(8080),
    makeBatScripts := Seq(),
    dockerUpdateLatest := true,
    libraryDependencies ++= Seq(
      Libraries.catsEffect,
      Libraries.http4sDsl,
      Libraries.http4sBlaze,
      Libraries.http4sCirce,
      Libraries.circeCore,
      Libraries.circeGeneric,
      Libraries.circeParser,
      Libraries.log4cats,
      Libraries.logback % Runtime,
      Libraries.derevoCore,
      Libraries.derevoCats,
      Libraries.derevoCirce,
      Libraries.refinedCore,
      Libraries.refinedCats,
      Libraries.newtype
    )
  )

addCommandAlias("runLinter", ";scalafixAll --rules OrganizeImports")