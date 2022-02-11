# Two Must-Have Tools for Jakarta Developers

### Dennis R. Gesker [dennis@candela.global](mailto:dennis@candela.global)

I love new toys. I really love new toys in technology stacks that are proven. I very much love new toys that allow me to
play with new technology that is anticipated in products that are proven. Toys that are tools are the best of all.

Here I am going to discuss two new toys that when used together may very well become a must-have arrow for your software
development quiver. These tools are the **wildfly-jar-maven-plugin** and the brand new **wildfly-datasources-preview-galleon-pack** from the Wildfly project.

Wildfly which is the upstream project for the JBoss Application Server from RedHat is a great solution for many
projects. The application server is Jakarta EE compatible with version 8 of the specification and newer “preview”
versions of the Wildfly Application Server are compatible with Jakarta EE version 9.1. It is my understanding that the
Wildfly project [has plans in place](https://www.wildfly.org/news/2022/01/21/WildFly-2022/) to be ready for the upcoming
release of Jakarta 10 and the plan indicates that the Wildfly project will likely issue future “preview” of their Application Server so that one can
take the next release of the standard for a spin. Exciting. For now, we’ll focus on Wildfly 26.0.1 PREVIEW --
released in the last week of January -- and Jakarta 9.1.

## First new toy

The **wildfly-jar-maven-plugin** allows one to provision (determine what parts, dependencies or capabilities your
application needs from the Application Server and perform any configuration required) a Wildfly server and generate an
**Uber Jar** that contains your application and the wildfly container using maven. That is right. The whole Application
Server and your Application bundled together in one tidy jar. Issue the “java -jar nameOfYourApplication-bootable.jar” and the
server fires up and your application is ready to go. Drop that bootable jar on a server on your LAN, deploy on your edge
device, deploy it in a containerized environment and, of course, in your target directory you’ll still find your
standard war file that holds just your application ready for deployment in the Jakarta EE container running on bare
metal or in a VM.

That is a lot of options and a whole bunch of config work and this plugin makes happen in a snap. This plugin appears to
have been pushed to the maven repository in 2020 and the most recent version, 7.0.0.Final, was pushed a couple of months
ago and from the frequency of releases in the maven repository the plugin is clearly actively maintained.

As above with the application server and your application bundled up tightly together you are a short hop away from a
trivial Docker file in preparation for deployment on Kubernetes. Adding the plugin to your maven pom.xml may look
something like this:

```xml
<plugin>
    <groupId>org.wildfly.plugins</groupId>
    <artifactId>**wildfly-jar-maven-plugin**</artifactId>
    <version>${version.wildfly.jar.maven.plugin}</version>
    <configuration>
        <log-time>true</log-time>
        <cloud>
            <type>kubernetes</type>
        </cloud>
        <context-root>false</context-root>
        <feature-packs>
            <feature-pack>
                <location>wildfly@maven(org.jboss.universe:community-universe)#${version.wildfly}
                </location>
            </feature-pack>
            <feature-pack>
                <groupId>org.wildfly</groupId>
                <artifactId>wildfly-datasources-galleon-pack</artifactId>
                <version>${version.wildfly.datasources.galleon-pack}</version>
            </feature-pack>
        </feature-packs>
        <hollow-jar>false</hollow-jar>
        <plugin-options>
            <jboss-fork-embedded>true</jboss-fork-embedded>
        </plugin-options>
        <!-- Listen on all ports -->
        <arguments>
            <argument>-b=0.0.0.0</argument>
        </arguments>
        <!-- Make sure we can debug -->
        <jvmArguments>
            <arg>-agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=n</arg>
        </jvmArguments>
        <cli-sessions>
            <cli-session>
                <!-- Feed Wildfly Some Properties -->
                <properties-file>package_properties.properties</properties-file>
                <!-- Run some jboss-cli.sh commands against Wildfly -->
                <script-files>
                    <script>package_script.cli</script>
                </script-files>
            </cli-session>
        </cli-sessions>
        <excluded-layers>
            <!-- Just here to demonstrate one can take layers OUT of the Container -->
            <layer>core-management</layer>
            <layer>deployment-scanner</layer>
            <layer>jmx-remoting</layer>
            <layer>request-controller</layer>
            <layer>security-manager</layer>
        </excluded-layers>
        <layers>
            <!-- Some of these layers are redundant as Galleon dependencies take's care of most things -->
            <layer>cloud-server</layer>
            <layer>ejb-dist-cache</layer>
            <layer>web-clustering</layer>
            <layer>ee</layer>
            <layer>ejb-lite</layer>
            <layer>jaxrs</layer>
            <layer>jpa</layer>
            <layer>jsf</layer>
            <layer>jsonb</layer>
            <layer>jsonp</layer>
            <!-- These go along with **wildfly-datasources-preview-galleon-pack** -->
            <layer>datasources-web-server</layer>
            <layer>postgresql-datasource</layer> <!-- Yes, there are other drivers available -->
        </layers>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>package</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

The full documentation for building bootable jars can be found [here](https://docs.wildfly.org/bootablejar/). To get you
going quickly I [posted some sample code](https://github.com/gesker/quick-db-web-app). Clone and issue the command:

```bash
mvn clean
mvn wildfly-jar:dev-watch
```

## Second new toy

But, we want to take the “preview” version of Wildfly for a spin. So we’ll update the “feature pack” location from:

```xml
<location>wildfly@maven(org.jboss.universe:community-universe)#${version.wildfly}</location>
```

To:

```xml
<location>wildfly-preview@maven(org.jboss.universe:community-universe)#${version.wildfly}</location>
```

We’ll also have to change the artifactId for the brand new **wildfly-datasources-preview-galleon-pack** so that we can
connect our application to a database. After all, what is a program without data.

Update

```xml
<artifactId>wildfly-datasources-galleon-pack</artifactId>
```

To

```xml
<artifactId>**wildfly-datasources-preview-galleon-pack**</artifactId>
```

Here I must thank the maintainers of
the [wildfly-extras](https://github.com/wildfly-extras/wildfly-datasources-galleon-pack) for making this available! This
was really a nice addition and necessary to try out a database driven application using the “preview” version of
Wildfly. It was pushed to maven in the first week of February. So, about a week ago.

Issue the

```bash
mvn wildfly-jar:dev-watch
```

command and you are up and running. Your code is being watched and deployed as you save your files by the
**wildfly-jar-maven-plugin**. And, with the **wildfly-datasources-preview-galleon-pack** connecting to your database is trivial.
Pretty nice.

A few tips if you take wildfly-preview along with the **wildfly-datasources-preview-galleon-pack** for a try using
**wildfly-jar-maven-plugin**…

* The “layers” configuration handles any dependencies you may need from Wildfly. If you know you are going to
  containerize, you can just start by adding the “cloud-server” layer and the “postgresql-datasource” or the datasource
  layer that matches your database.
* If there are “layers” you do not need you can exclude them in order to thin up yourUber Jar. I excluded a few in my
  plugin configuration just to demonstrate that ability of the **wildfly-jar-maven-plugin**. Checkout the available layers
  and determine which are optional or not necessary for your
  needs [here](https://docs.wildfly.org/21/Galleon_Guide.html#wildfly_galleon_layers).
* You’ll want to update the javax namespace in your application to jakarta. The regular version of wildfly will allow
  you to use either the older javax namespace or the newer jakarta namespace but the preview version is focused on
  Jakarta so you’ll need to consistently use import jakarta.* packages in your java source code. Same for your web.xml
  file if that applies to your project.
* Keep in mind that this is a PREVIEW so it may very well be best to use the regular version in production. That being
  said so far the preview has been working well for me.

One essential tip!

* You will notice that no where did we put in settings for the application to find our database server and the database
  within that server. The **wildfly-datasources-preview-galleon-pack** will read this information from environment
  variables. Now, you can set properties inside Wildfly as well but in practice I'd recommend environment variables.
  This lends itself to containerization of the Application Server / Application Uber Jar.

Example of database environment variables on Linux:

```bash
export POSTGRESQL_HOST=server.withyourdatabase.com
export POSTGRESQL_PORT=5432
export POSTGRESQL_DATABASE=quickDb
export POSTGRESQL_USER=yourUsername
export POSTGRESQL_PASSWORD=yourSecretPassword
```

After coming across the plugin and then getting help from the wildfly community to ensure that I could connect to a
database using the preview version this combination of tools quickly hit a “can’t live without” status and then had to
chide myself with “How did I miss this?” In combination these Galleon “feature packs” and the **wildfly-jar-maven-plugin** a really nice experience for the developer can be enjoyed. Especially mvn wildfly-jar:dev-watch!

Still, as useful as "mvn wildfly-jar:dev-watch" is when under active development the goal is to get to a single database
aware jar that has all the capabilities inherent in Wildfly ready for deployment. With this plugin this is simple
enough. Issue the command:

```bash
mvn clean package wildfly-jar:package
```

Cool. Toys. Indeed.


## Configuration Items to Consider

There are a few configuration issues that you “might” have to work around or at least about which you may need a plan depending on how you intend to use and deploy your application when you make use of the **wildfly-jar-maven-plugin**. You will see that stubbed into the sample code I have provided there are a couple of (almost) empty files that can be used to add application environment variables and issue some additional configuration commands to complete your setup of Wildfly.

You will see that in the package_script.cli file I add the line

```prototext
/system-property=somePropertyName:add(value=somePropertyValue)
```
which sets the property “somePropertyName” to “somePropertyValue. Adding this line is essentially the same as issuing

```bash
${JBOSS_HOME}/bin/jboss-cli.sh /system-property=somePropertyName:add(value=somePropertyValue)
```
on the command line. Still, even with this flexibility I haven’t yet determined the **best practice** for some configuration tasks. Not that there aren’t ways to address these configuration issues but you will need a plan.

Take for example a scenario where one needs to keep credentials secure. If your application is going into a Kubernetes environment you’ll likely use something like [Bitnami’s Sealed Secrets](https://github.com/bitnami-labs/sealed-secrets) or [HashiCorp’s Vault](https://www.vaultproject.io/) and so this is not a challenge. On your workstation or server where you have the control of the operating environment your application can reference this environment. But, what if what is best for your application would be to store these credentials INSIDE the Wildfly container. Wildfly has the excellent security subsystem, Elytron, and it would be natural to use the tool. The ability your credentials INSIDE Wildfly and the facility, Elytron, is there and so it should be considered.

On your workstation or server you would add a “secret-key-credential-store.” This might go something like:

```bash
history --disable
/subsystem=elytron/secret-key-credential-store=secret-key-credential-store:add(relative-to=jboss.server.config.dir, path=secretKeyCredentialStore.cs)
/subsystem=elytron/secret-key-credential-store=secret-key-credential-store:export-secret-key(alias=key)
/subsystem=elytron/expression=encryption:add(resolvers=[{name=secret-key-resolver, credential-store=secret-key-credential-store, secret-key=key}])
```

So far so good. But, the first obstacle in this “secure the credentials INSIDE Wildfly” configuration task presents itself. The next two commands that one would need to issue, creating an expression for your clear text password (the INPUT) to the first command and adding the OUTPUT of this command to a “credential-store” are dependent.

```bash
/subsystem=elytron/expression=encryption:create-expression(resolver=secret-key-resolver, clear-text=passwordThatNoOneWillEverGuess)

/subsystem=elytron/credential-store=credential-store:add(relative-to=jboss.server.config.dir, path=credentialStoreCandela.cs, credential-reference={clear-text= *OUTPUT OF PREVIOUS COMMAND AS INPUT HERE* }, create=true)
```

Chaining outputs from one command to another when you have a shell is no big deal. Or, at worst this is a once in a while system administration task and you will just cut and paste the expression from the first command into the second. Then as one continues to complete the task it is also pretty straightforward as you complete

```bash
/subsystem=elytron/credential-store=credential-store:generate-secret-key(alias=key)
/subsystem=elytron/expression=encryption:list-add(name=resolvers, value={name=resolver, credential-store=credential-store, secret-key=key})
reload
/subsystem=elytron/expression=encryption:write-attribute(name=default-resolver, value=resolver)
reload
/subsystem=elytron/expression=encryption:create-expression(clear-text=passwordThatNoOneWillEverGuess)
history --enable
/subsystem=elytron/credential-store=credential-store:add-alias(alias=MyFirstCredential, secret-value=doNotShareThis)
/subsystem=elytron/credential-store=credential-store:add-alias(alias=MySecondCredential, secret-value=AlsoDoNotShareThis)
/subsystem=elytron/secret-key-credential-store=secret-key-credential-store:read-aliases
/subsystem=elytron/credential-store=credential-store:read-aliases
```

The “reload” commands above are necessary to complete the task and again no big deal on a workstation or server. Completing this task ON THE FLY as is the capability that the “mvn wildfly-jar:watch-dev” command and the “mvn clean package wildfly-jar:package” provide as the process appears to be “go forward.” Possible ways to approach a mildly complex configuration like this? Perhaps just copy in a configuration file and the appropriate supporting files in a chosen maven phase. Perhaps see how far you can get by populating the package_script.cli and package_properties.properties with your particulars.

What would be ideal? I am not yet certain given my relatively short experience with the **wildfly-jar-maven-plugin**. My initial thoughts are that the wildfly-preview-datasources-galleon-pack is SO SLICK that maybe a “wildfly-credential-store-galleon-pack” or some such artifact could be created in order to address this specific scenario. Add the galleon-pack to the plugin configuration and let it read a whole number of environment variables (maybe with a predefined naming convention?) on the development workstation and store these credentials inside Elytron inside Wildfly ON THE FLY just as the wildfly-preview-datasources-galleon-pack deals with database connectivity ON THE FLY.

Again, I just chose securing credentials INSIDE Wildfly as a scenario where additional planning is required if you are going to use the plugin. I am sure you can imagine other configuration tasks for your application depending on your deployment needs should you bundle it in a bootable jar.



## Enough is never enough

This is so cool. What more could you want? Glad you asked and what kind of end user would I be if I didn't want more?

I would like to use the “vertx.io” drivers for connecting to my database and hope these make it into the
**wildfly-datasources-preview-galleon-pack**. This wish presupposes that a new version of Hibernate, the JPA and ORM
implementation in Wildfly, that pairs up with Hibernate Reactive. The Hibernate website indicates that this would be
version 5.6. Hibernate 5.3 is the version packaged with wildfly-preview. Perhaps this becomes a non-issue
if [Hibernate ORM 6](https://hibernate.org/orm/releases/6.0/) reaches stable status in time
for [Jakarta 10](https://jakarta.ee/specifications/platform/10/). That Hibernate Reactive goodness is found in projects
like [Quarkus](https://quarkus.io/) and I’d love to see those capabilities available in Wildfly. That is as long as
these additions do not break compliance with the Jakarta standard and the standard doesn't preclude more. 

There is a [Wildfly MicroProfile Reactive Feature Pack](https://github.com/wildfly-extras/wildfly-mp-reactive-feature-pack) but I
didn't see any specific references to Hibernate Reactive like Panache in the project repository.

## Finally

These are not just new toys but useful tools. Try out the **wildfly-jar-maven-plugin** and with it the
**wildfly-datasources-preview-galleon-pack** if you are checking out wildfly-preview and need to communicate with your
database.

I posted a small sample project up on [github here](https://github.com/gesker/quick-db-web-app) if you’d like to take this combination of new tools for a try and
are in a hurry. That trivial Dockerfile mentioned earlier and, for good measure, a sample Kubernetes yaml file are in the repository, too. The sample code has a reference sql file, configured JPA persistence unit and JPA Object and a JSF page to display data if you choose to populate the table. The **wildfly-datasources-preview-galleon-pack** get's your bootable jar to be database aware with very little effort so we might as fetch some data and display it. 

Thanks again to the  [wildfly-extras](https://github.com/wildfly-extras/wildfly-datasources-galleon-pack) team. So many good tools come out of the Wildfly and these tools are not the least of these good things and each adds to the pleasure of developing applications using [Jakarta](https://jakarta.ee)!

---
### Author
[Dennis](https://www.linkedin.com/in/gesker/) is a long time Java developer and member of
the [Jakarta Ambassadors](https://jakartaee-ambassadors.io/). Dennis is currently working on
the [Candela Global](https://candela.global/) project which is a Social Media Fin-Tech initiative launching later this
year. Feel free to drop Dennis a line at [dennis@candela.global](mailto:dennis@candela.global) or [dennis@gesker.com](mailto:dennis@gesker.com).
