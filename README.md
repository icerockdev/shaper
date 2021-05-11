# Shaper

Kotlin library and command line tool for generation of directory with files from template (on
Handlebars) and configuration.

# Install

`/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/icerockdev/shaper/master/install-shaper.sh)"`

# Usage

## CLI

You can use generator by CLI app. Configuration stored in yaml file in this case:

```yaml
globalParams:
  packageName: dev.icerock

files:
  - pathTemplate: 'build.gradle.kts'
    contentTemplateName: build.gradle.kts
    templateParams:
      dependencies:
        - dep1
        - dep2
  - pathTemplate: 'src/main/kotlin/{{packagePath packageName}}/Test.kt'
    contentTemplateName: Test.kt
    templateParams:
      packageName: dev.icerock.test
```

To run CLI:

```shell
shaper-cli --help
```

## Core

Core is kotlin library, for integration in cli-app, idea plugin, gradle plugin, etc.

Using is simple:

```kotlin
import dev.icerock.tools.shaper.core.TemplateConfig
import dev.icerock.tools.shaper.core.Shaper

// describe files configuration for generator
val buildGradleFile = TemplateConfig.FileConfig(
    // path of output file
    pathTemplate = "build.gradle.kts",
    // name of template file used for content of file
    contentTemplateName = "build.gradle.kts",
    // params that will be passed into template
    templateParams = mapOf("dependencies" to listOf("dep1", "dep2"))
)
val sourceCodeFile = TemplateConfig.FileConfig(
    // path also support templates and got all params
    pathTemplate = "src/main/kotlin/{{packagePath packageName}}/Test.kt",
    contentTemplateName = "Test.kt",
    templateParams = mapOf("packageName" to "dev.icerock.test")
)

// describe generator config
val config = TemplateConfig(
    // params that will be passed into template of each files. 
    // can be overriden by params of file
    globalParams = mapOf("packageName" to "dev.icerock"),
    // list of files that should be generated
    files = listOf(buildGradleFile, sourceCodeFile)
)

// create generator with configuration
val shaper = Shaper(config)
// execute generation into build/test directory
shaper.execute(output = "build/test")
```

Templates will be loaded by priority:

1. search at working dir by name
2. search at template repositories from `~/.shaper/config.yaml`
2. search at resources of ClassLoader by name

Here sample templates:
`build.gradle.kts.hbs`:

```handlebars
plugins {
id("org.jetbrains.kotlin.jvm") version "1.4.30"
}

dependencies {
{{#each dependencies}}    implementation("{{this}}")
{{/each}}
}
```

`Test.kt.hbs`:

```handlebars
package {{packageName}}

class Test {
}
```

## Include/exclude files

You can include/exclude files using global parameters in config file.

1. Add global parameter(s) that can have value `true` or `false`.

2. Add `{{incl ...}}` helper with the specified parameter in file path and/or file name.

```yaml
globalParams:
  ...
  addTests: false
  addDefaultTest: false
files:
  - pathTemplate: '{{incl addTests}}src/main/kotlin/package/{{incl addDefaultTest}}DefaultTest.kt'
    contentTemplateName: DefaultTest.kt
```

In this example, file `DefaultTest.kt` will be added only if both parameters `addFragment` and `addDefaultTest` are set to `true`.

## Partials

For use partials you can put your `.hbs` files in `includes` directories (multi-nesting support). 

In config yaml file:
```yaml
globalParams:
  ...
files:
  ...
outputs:
  ...
includes:
  - partials
  - layouts

```

`partials/sub-partials/type.hbs`
```handlebars
{{~#if (eq this.type "integer")}}Int{{else}}{{stu this.type}}{{/if}}{{#if nullableOnly}}?{{else}}{{#if this.nullable}}?{{/if~}}{{/if~}}
```

`layouts/hello.hbs`
```handlebars
Hi {{name}}
```

And use the partials in templates: 
```handlebars
{{~#each columns}}
    val {{this.name}}: {{> type this nullableOnly=true}} = obj.{{this.name~}}
{{~/each}}
{{> hello name="Developer"}}
```

## Helpers
In config yaml file:
```yaml
globalParams:
  items:
    - name: "name1"
      type: "type1"
      ...
    - name: "name2"
      type: "type2"
  ...
```
`filterByAllOf` filters `items` by all pair key-value (AND condition), for example:
```handlebars
{{~#each (filterByAllOf items type="type1" name="name1")}}
{{! there will only be items when type="type1" and name="name1"}}
{{~/each}}
```
`filterByOneOf` filters `items` by one of pair key-value (OR condition), for example:
```handlebars
{{~#each (filterByAllOf items type="type1" name="name2")}}
{{! there will only be items when type="type1" or name="name2"}}
{{~/each}}
```
`containsAllOf` checks for all pair key-value (AND condition) in `items`, for example:
```handlebars
{{~#if (containsAllOf items type="type1" name="name1")}}
...
{{~/if}}
```
`containsOneOf` checks for one of pair key-value (OR condition) in `items`, for example:
```handlebars
{{~#if (containsOneOf items type="type1" name="name2")}}
...
{{~/if}}
```
`containsKey` checks for exist key in `items`, for example:
```handlebars
{{~#if (containsKey items "type")}}
...
{{~/if}}
```
`containsValue` checks for exist value in `items`, for example:
```handlebars
{{~#if (containsValue items "type1")}}
...
{{~/if}}
```

## License

    Copyright 2021 IceRock MAG Inc
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
