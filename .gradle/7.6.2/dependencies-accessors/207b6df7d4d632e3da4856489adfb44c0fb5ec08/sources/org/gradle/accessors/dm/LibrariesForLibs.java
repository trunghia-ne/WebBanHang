package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
*/
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final ChLibraryAccessors laccForChLibraryAccessors = new ChLibraryAccessors(owner);
    private final ComLibraryAccessors laccForComLibraryAccessors = new ComLibraryAccessors(owner);
    private final CommonsLibraryAccessors laccForCommonsLibraryAccessors = new CommonsLibraryAccessors(owner);
    private final JakartaLibraryAccessors laccForJakartaLibraryAccessors = new JakartaLibraryAccessors(owner);
    private final OrgLibraryAccessors laccForOrgLibraryAccessors = new OrgLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Returns the group of libraries at ch
     */
    public ChLibraryAccessors getCh() { return laccForChLibraryAccessors; }

    /**
     * Returns the group of libraries at com
     */
    public ComLibraryAccessors getCom() { return laccForComLibraryAccessors; }

    /**
     * Returns the group of libraries at commons
     */
    public CommonsLibraryAccessors getCommons() { return laccForCommonsLibraryAccessors; }

    /**
     * Returns the group of libraries at jakarta
     */
    public JakartaLibraryAccessors getJakarta() { return laccForJakartaLibraryAccessors; }

    /**
     * Returns the group of libraries at org
     */
    public OrgLibraryAccessors getOrg() { return laccForOrgLibraryAccessors; }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() { return vaccForVersionAccessors; }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() { return baccForBundleAccessors; }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() { return paccForPluginAccessors; }

    public static class ChLibraryAccessors extends SubDependencyFactory {
        private final ChQosLibraryAccessors laccForChQosLibraryAccessors = new ChQosLibraryAccessors(owner);

        public ChLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at ch.qos
         */
        public ChQosLibraryAccessors getQos() { return laccForChQosLibraryAccessors; }

    }

    public static class ChQosLibraryAccessors extends SubDependencyFactory {
        private final ChQosLogbackLibraryAccessors laccForChQosLogbackLibraryAccessors = new ChQosLogbackLibraryAccessors(owner);

        public ChQosLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at ch.qos.logback
         */
        public ChQosLogbackLibraryAccessors getLogback() { return laccForChQosLogbackLibraryAccessors; }

    }

    public static class ChQosLogbackLibraryAccessors extends SubDependencyFactory {
        private final ChQosLogbackLogbackLibraryAccessors laccForChQosLogbackLogbackLibraryAccessors = new ChQosLogbackLogbackLibraryAccessors(owner);

        public ChQosLogbackLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at ch.qos.logback.logback
         */
        public ChQosLogbackLogbackLibraryAccessors getLogback() { return laccForChQosLogbackLogbackLibraryAccessors; }

    }

    public static class ChQosLogbackLogbackLibraryAccessors extends SubDependencyFactory {

        public ChQosLogbackLogbackLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for classic (ch.qos.logback:logback-classic)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getClassic() { return create("ch.qos.logback.logback.classic"); }

    }

    public static class ComLibraryAccessors extends SubDependencyFactory {
        private final ComCloudinaryLibraryAccessors laccForComCloudinaryLibraryAccessors = new ComCloudinaryLibraryAccessors(owner);
        private final ComFasterxmlLibraryAccessors laccForComFasterxmlLibraryAccessors = new ComFasterxmlLibraryAccessors(owner);
        private final ComGoogleLibraryAccessors laccForComGoogleLibraryAccessors = new ComGoogleLibraryAccessors(owner);
        private final ComMysqlLibraryAccessors laccForComMysqlLibraryAccessors = new ComMysqlLibraryAccessors(owner);
        private final ComSunLibraryAccessors laccForComSunLibraryAccessors = new ComSunLibraryAccessors(owner);

        public ComLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.cloudinary
         */
        public ComCloudinaryLibraryAccessors getCloudinary() { return laccForComCloudinaryLibraryAccessors; }

        /**
         * Returns the group of libraries at com.fasterxml
         */
        public ComFasterxmlLibraryAccessors getFasterxml() { return laccForComFasterxmlLibraryAccessors; }

        /**
         * Returns the group of libraries at com.google
         */
        public ComGoogleLibraryAccessors getGoogle() { return laccForComGoogleLibraryAccessors; }

        /**
         * Returns the group of libraries at com.mysql
         */
        public ComMysqlLibraryAccessors getMysql() { return laccForComMysqlLibraryAccessors; }

        /**
         * Returns the group of libraries at com.sun
         */
        public ComSunLibraryAccessors getSun() { return laccForComSunLibraryAccessors; }

    }

    public static class ComCloudinaryLibraryAccessors extends SubDependencyFactory {
        private final ComCloudinaryCloudinaryLibraryAccessors laccForComCloudinaryCloudinaryLibraryAccessors = new ComCloudinaryCloudinaryLibraryAccessors(owner);

        public ComCloudinaryLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.cloudinary.cloudinary
         */
        public ComCloudinaryCloudinaryLibraryAccessors getCloudinary() { return laccForComCloudinaryCloudinaryLibraryAccessors; }

    }

    public static class ComCloudinaryCloudinaryLibraryAccessors extends SubDependencyFactory {

        public ComCloudinaryCloudinaryLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for http44 (com.cloudinary:cloudinary-http44)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getHttp44() { return create("com.cloudinary.cloudinary.http44"); }

    }

    public static class ComFasterxmlLibraryAccessors extends SubDependencyFactory {
        private final ComFasterxmlJacksonLibraryAccessors laccForComFasterxmlJacksonLibraryAccessors = new ComFasterxmlJacksonLibraryAccessors(owner);

        public ComFasterxmlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.fasterxml.jackson
         */
        public ComFasterxmlJacksonLibraryAccessors getJackson() { return laccForComFasterxmlJacksonLibraryAccessors; }

    }

    public static class ComFasterxmlJacksonLibraryAccessors extends SubDependencyFactory {
        private final ComFasterxmlJacksonCoreLibraryAccessors laccForComFasterxmlJacksonCoreLibraryAccessors = new ComFasterxmlJacksonCoreLibraryAccessors(owner);

        public ComFasterxmlJacksonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.fasterxml.jackson.core
         */
        public ComFasterxmlJacksonCoreLibraryAccessors getCore() { return laccForComFasterxmlJacksonCoreLibraryAccessors; }

    }

    public static class ComFasterxmlJacksonCoreLibraryAccessors extends SubDependencyFactory {
        private final ComFasterxmlJacksonCoreJacksonLibraryAccessors laccForComFasterxmlJacksonCoreJacksonLibraryAccessors = new ComFasterxmlJacksonCoreJacksonLibraryAccessors(owner);

        public ComFasterxmlJacksonCoreLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.fasterxml.jackson.core.jackson
         */
        public ComFasterxmlJacksonCoreJacksonLibraryAccessors getJackson() { return laccForComFasterxmlJacksonCoreJacksonLibraryAccessors; }

    }

    public static class ComFasterxmlJacksonCoreJacksonLibraryAccessors extends SubDependencyFactory {

        public ComFasterxmlJacksonCoreJacksonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for databind (com.fasterxml.jackson.core:jackson-databind)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getDatabind() { return create("com.fasterxml.jackson.core.jackson.databind"); }

    }

    public static class ComGoogleLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleApiLibraryAccessors laccForComGoogleApiLibraryAccessors = new ComGoogleApiLibraryAccessors(owner);
        private final ComGoogleApisLibraryAccessors laccForComGoogleApisLibraryAccessors = new ComGoogleApisLibraryAccessors(owner);
        private final ComGoogleCodeLibraryAccessors laccForComGoogleCodeLibraryAccessors = new ComGoogleCodeLibraryAccessors(owner);
        private final ComGoogleGuavaLibraryAccessors laccForComGoogleGuavaLibraryAccessors = new ComGoogleGuavaLibraryAccessors(owner);
        private final ComGoogleHttpLibraryAccessors laccForComGoogleHttpLibraryAccessors = new ComGoogleHttpLibraryAccessors(owner);
        private final ComGoogleOauthLibraryAccessors laccForComGoogleOauthLibraryAccessors = new ComGoogleOauthLibraryAccessors(owner);

        public ComGoogleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.api
         */
        public ComGoogleApiLibraryAccessors getApi() { return laccForComGoogleApiLibraryAccessors; }

        /**
         * Returns the group of libraries at com.google.apis
         */
        public ComGoogleApisLibraryAccessors getApis() { return laccForComGoogleApisLibraryAccessors; }

        /**
         * Returns the group of libraries at com.google.code
         */
        public ComGoogleCodeLibraryAccessors getCode() { return laccForComGoogleCodeLibraryAccessors; }

        /**
         * Returns the group of libraries at com.google.guava
         */
        public ComGoogleGuavaLibraryAccessors getGuava() { return laccForComGoogleGuavaLibraryAccessors; }

        /**
         * Returns the group of libraries at com.google.http
         */
        public ComGoogleHttpLibraryAccessors getHttp() { return laccForComGoogleHttpLibraryAccessors; }

        /**
         * Returns the group of libraries at com.google.oauth
         */
        public ComGoogleOauthLibraryAccessors getOauth() { return laccForComGoogleOauthLibraryAccessors; }

    }

    public static class ComGoogleApiLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleApiClientLibraryAccessors laccForComGoogleApiClientLibraryAccessors = new ComGoogleApiClientLibraryAccessors(owner);

        public ComGoogleApiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.api.client
         */
        public ComGoogleApiClientLibraryAccessors getClient() { return laccForComGoogleApiClientLibraryAccessors; }

    }

    public static class ComGoogleApiClientLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleApiClientGoogleLibraryAccessors laccForComGoogleApiClientGoogleLibraryAccessors = new ComGoogleApiClientGoogleLibraryAccessors(owner);

        public ComGoogleApiClientLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.api.client.google
         */
        public ComGoogleApiClientGoogleLibraryAccessors getGoogle() { return laccForComGoogleApiClientGoogleLibraryAccessors; }

    }

    public static class ComGoogleApiClientGoogleLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleApiClientGoogleApiLibraryAccessors laccForComGoogleApiClientGoogleApiLibraryAccessors = new ComGoogleApiClientGoogleApiLibraryAccessors(owner);

        public ComGoogleApiClientGoogleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.api.client.google.api
         */
        public ComGoogleApiClientGoogleApiLibraryAccessors getApi() { return laccForComGoogleApiClientGoogleApiLibraryAccessors; }

    }

    public static class ComGoogleApiClientGoogleApiLibraryAccessors extends SubDependencyFactory {

        public ComGoogleApiClientGoogleApiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for client (com.google.api-client:google-api-client)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getClient() { return create("com.google.api.client.google.api.client"); }

    }

    public static class ComGoogleApisLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleApisGoogleLibraryAccessors laccForComGoogleApisGoogleLibraryAccessors = new ComGoogleApisGoogleLibraryAccessors(owner);

        public ComGoogleApisLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.apis.google
         */
        public ComGoogleApisGoogleLibraryAccessors getGoogle() { return laccForComGoogleApisGoogleLibraryAccessors; }

    }

    public static class ComGoogleApisGoogleLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleApisGoogleApiLibraryAccessors laccForComGoogleApisGoogleApiLibraryAccessors = new ComGoogleApisGoogleApiLibraryAccessors(owner);

        public ComGoogleApisGoogleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.apis.google.api
         */
        public ComGoogleApisGoogleApiLibraryAccessors getApi() { return laccForComGoogleApisGoogleApiLibraryAccessors; }

    }

    public static class ComGoogleApisGoogleApiLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleApisGoogleApiServicesLibraryAccessors laccForComGoogleApisGoogleApiServicesLibraryAccessors = new ComGoogleApisGoogleApiServicesLibraryAccessors(owner);

        public ComGoogleApisGoogleApiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.apis.google.api.services
         */
        public ComGoogleApisGoogleApiServicesLibraryAccessors getServices() { return laccForComGoogleApisGoogleApiServicesLibraryAccessors; }

    }

    public static class ComGoogleApisGoogleApiServicesLibraryAccessors extends SubDependencyFactory {

        public ComGoogleApisGoogleApiServicesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for oauth2 (com.google.apis:google-api-services-oauth2)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getOauth2() { return create("com.google.apis.google.api.services.oauth2"); }

    }

    public static class ComGoogleCodeLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleCodeGsonLibraryAccessors laccForComGoogleCodeGsonLibraryAccessors = new ComGoogleCodeGsonLibraryAccessors(owner);

        public ComGoogleCodeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.code.gson
         */
        public ComGoogleCodeGsonLibraryAccessors getGson() { return laccForComGoogleCodeGsonLibraryAccessors; }

    }

    public static class ComGoogleCodeGsonLibraryAccessors extends SubDependencyFactory {

        public ComGoogleCodeGsonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for gson (com.google.code.gson:gson)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGson() { return create("com.google.code.gson.gson"); }

    }

    public static class ComGoogleGuavaLibraryAccessors extends SubDependencyFactory {

        public ComGoogleGuavaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for guava (com.google.guava:guava)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGuava() { return create("com.google.guava.guava"); }

    }

    public static class ComGoogleHttpLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleHttpClientLibraryAccessors laccForComGoogleHttpClientLibraryAccessors = new ComGoogleHttpClientLibraryAccessors(owner);

        public ComGoogleHttpLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.http.client
         */
        public ComGoogleHttpClientLibraryAccessors getClient() { return laccForComGoogleHttpClientLibraryAccessors; }

    }

    public static class ComGoogleHttpClientLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleHttpClientGoogleLibraryAccessors laccForComGoogleHttpClientGoogleLibraryAccessors = new ComGoogleHttpClientGoogleLibraryAccessors(owner);

        public ComGoogleHttpClientLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.http.client.google
         */
        public ComGoogleHttpClientGoogleLibraryAccessors getGoogle() { return laccForComGoogleHttpClientGoogleLibraryAccessors; }

    }

    public static class ComGoogleHttpClientGoogleLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleHttpClientGoogleHttpLibraryAccessors laccForComGoogleHttpClientGoogleHttpLibraryAccessors = new ComGoogleHttpClientGoogleHttpLibraryAccessors(owner);

        public ComGoogleHttpClientGoogleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.http.client.google.http
         */
        public ComGoogleHttpClientGoogleHttpLibraryAccessors getHttp() { return laccForComGoogleHttpClientGoogleHttpLibraryAccessors; }

    }

    public static class ComGoogleHttpClientGoogleHttpLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleHttpClientGoogleHttpClientLibraryAccessors laccForComGoogleHttpClientGoogleHttpClientLibraryAccessors = new ComGoogleHttpClientGoogleHttpClientLibraryAccessors(owner);

        public ComGoogleHttpClientGoogleHttpLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.http.client.google.http.client
         */
        public ComGoogleHttpClientGoogleHttpClientLibraryAccessors getClient() { return laccForComGoogleHttpClientGoogleHttpClientLibraryAccessors; }

    }

    public static class ComGoogleHttpClientGoogleHttpClientLibraryAccessors extends SubDependencyFactory {

        public ComGoogleHttpClientGoogleHttpClientLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for jackson2 (com.google.http-client:google-http-client-jackson2)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJackson2() { return create("com.google.http.client.google.http.client.jackson2"); }

    }

    public static class ComGoogleOauthLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleOauthClientLibraryAccessors laccForComGoogleOauthClientLibraryAccessors = new ComGoogleOauthClientLibraryAccessors(owner);

        public ComGoogleOauthLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.oauth.client
         */
        public ComGoogleOauthClientLibraryAccessors getClient() { return laccForComGoogleOauthClientLibraryAccessors; }

    }

    public static class ComGoogleOauthClientLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleOauthClientGoogleLibraryAccessors laccForComGoogleOauthClientGoogleLibraryAccessors = new ComGoogleOauthClientGoogleLibraryAccessors(owner);

        public ComGoogleOauthClientLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.oauth.client.google
         */
        public ComGoogleOauthClientGoogleLibraryAccessors getGoogle() { return laccForComGoogleOauthClientGoogleLibraryAccessors; }

    }

    public static class ComGoogleOauthClientGoogleLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleOauthClientGoogleOauthLibraryAccessors laccForComGoogleOauthClientGoogleOauthLibraryAccessors = new ComGoogleOauthClientGoogleOauthLibraryAccessors(owner);

        public ComGoogleOauthClientGoogleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.oauth.client.google.oauth
         */
        public ComGoogleOauthClientGoogleOauthLibraryAccessors getOauth() { return laccForComGoogleOauthClientGoogleOauthLibraryAccessors; }

    }

    public static class ComGoogleOauthClientGoogleOauthLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleOauthClientGoogleOauthClientLibraryAccessors laccForComGoogleOauthClientGoogleOauthClientLibraryAccessors = new ComGoogleOauthClientGoogleOauthClientLibraryAccessors(owner);

        public ComGoogleOauthClientGoogleOauthLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.google.oauth.client.google.oauth.client
         */
        public ComGoogleOauthClientGoogleOauthClientLibraryAccessors getClient() { return laccForComGoogleOauthClientGoogleOauthClientLibraryAccessors; }

    }

    public static class ComGoogleOauthClientGoogleOauthClientLibraryAccessors extends SubDependencyFactory {

        public ComGoogleOauthClientGoogleOauthClientLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for jetty (com.google.oauth-client:google-oauth-client-jetty)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJetty() { return create("com.google.oauth.client.google.oauth.client.jetty"); }

    }

    public static class ComMysqlLibraryAccessors extends SubDependencyFactory {
        private final ComMysqlMysqlLibraryAccessors laccForComMysqlMysqlLibraryAccessors = new ComMysqlMysqlLibraryAccessors(owner);

        public ComMysqlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.mysql.mysql
         */
        public ComMysqlMysqlLibraryAccessors getMysql() { return laccForComMysqlMysqlLibraryAccessors; }

    }

    public static class ComMysqlMysqlLibraryAccessors extends SubDependencyFactory {
        private final ComMysqlMysqlConnectorLibraryAccessors laccForComMysqlMysqlConnectorLibraryAccessors = new ComMysqlMysqlConnectorLibraryAccessors(owner);

        public ComMysqlMysqlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.mysql.mysql.connector
         */
        public ComMysqlMysqlConnectorLibraryAccessors getConnector() { return laccForComMysqlMysqlConnectorLibraryAccessors; }

    }

    public static class ComMysqlMysqlConnectorLibraryAccessors extends SubDependencyFactory {

        public ComMysqlMysqlConnectorLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for j (com.mysql:mysql-connector-j)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJ() { return create("com.mysql.mysql.connector.j"); }

    }

    public static class ComSunLibraryAccessors extends SubDependencyFactory {
        private final ComSunMailLibraryAccessors laccForComSunMailLibraryAccessors = new ComSunMailLibraryAccessors(owner);

        public ComSunLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.sun.mail
         */
        public ComSunMailLibraryAccessors getMail() { return laccForComSunMailLibraryAccessors; }

    }

    public static class ComSunMailLibraryAccessors extends SubDependencyFactory {
        private final ComSunMailJavaxLibraryAccessors laccForComSunMailJavaxLibraryAccessors = new ComSunMailJavaxLibraryAccessors(owner);

        public ComSunMailLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.sun.mail.javax
         */
        public ComSunMailJavaxLibraryAccessors getJavax() { return laccForComSunMailJavaxLibraryAccessors; }

    }

    public static class ComSunMailJavaxLibraryAccessors extends SubDependencyFactory {

        public ComSunMailJavaxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for mail (com.sun.mail:javax.mail)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getMail() { return create("com.sun.mail.javax.mail"); }

    }

    public static class CommonsLibraryAccessors extends SubDependencyFactory {
        private final CommonsIoLibraryAccessors laccForCommonsIoLibraryAccessors = new CommonsIoLibraryAccessors(owner);

        public CommonsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at commons.io
         */
        public CommonsIoLibraryAccessors getIo() { return laccForCommonsIoLibraryAccessors; }

    }

    public static class CommonsIoLibraryAccessors extends SubDependencyFactory {
        private final CommonsIoCommonsLibraryAccessors laccForCommonsIoCommonsLibraryAccessors = new CommonsIoCommonsLibraryAccessors(owner);

        public CommonsIoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at commons.io.commons
         */
        public CommonsIoCommonsLibraryAccessors getCommons() { return laccForCommonsIoCommonsLibraryAccessors; }

    }

    public static class CommonsIoCommonsLibraryAccessors extends SubDependencyFactory {

        public CommonsIoCommonsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for io (commons-io:commons-io)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getIo() { return create("commons.io.commons.io"); }

    }

    public static class JakartaLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletLibraryAccessors laccForJakartaServletLibraryAccessors = new JakartaServletLibraryAccessors(owner);

        public JakartaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at jakarta.servlet
         */
        public JakartaServletLibraryAccessors getServlet() { return laccForJakartaServletLibraryAccessors; }

    }

    public static class JakartaServletLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJakartaLibraryAccessors laccForJakartaServletJakartaLibraryAccessors = new JakartaServletJakartaLibraryAccessors(owner);
        private final JakartaServletJspLibraryAccessors laccForJakartaServletJspLibraryAccessors = new JakartaServletJspLibraryAccessors(owner);

        public JakartaServletLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at jakarta.servlet.jakarta
         */
        public JakartaServletJakartaLibraryAccessors getJakarta() { return laccForJakartaServletJakartaLibraryAccessors; }

        /**
         * Returns the group of libraries at jakarta.servlet.jsp
         */
        public JakartaServletJspLibraryAccessors getJsp() { return laccForJakartaServletJspLibraryAccessors; }

    }

    public static class JakartaServletJakartaLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJakartaServletLibraryAccessors laccForJakartaServletJakartaServletLibraryAccessors = new JakartaServletJakartaServletLibraryAccessors(owner);

        public JakartaServletJakartaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at jakarta.servlet.jakarta.servlet
         */
        public JakartaServletJakartaServletLibraryAccessors getServlet() { return laccForJakartaServletJakartaServletLibraryAccessors; }

    }

    public static class JakartaServletJakartaServletLibraryAccessors extends SubDependencyFactory {

        public JakartaServletJakartaServletLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for api (jakarta.servlet:jakarta.servlet-api)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getApi() { return create("jakarta.servlet.jakarta.servlet.api"); }

    }

    public static class JakartaServletJspLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJspJstlLibraryAccessors laccForJakartaServletJspJstlLibraryAccessors = new JakartaServletJspJstlLibraryAccessors(owner);

        public JakartaServletJspLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at jakarta.servlet.jsp.jstl
         */
        public JakartaServletJspJstlLibraryAccessors getJstl() { return laccForJakartaServletJspJstlLibraryAccessors; }

    }

    public static class JakartaServletJspJstlLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJspJstlJakartaLibraryAccessors laccForJakartaServletJspJstlJakartaLibraryAccessors = new JakartaServletJspJstlJakartaLibraryAccessors(owner);

        public JakartaServletJspJstlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at jakarta.servlet.jsp.jstl.jakarta
         */
        public JakartaServletJspJstlJakartaLibraryAccessors getJakarta() { return laccForJakartaServletJspJstlJakartaLibraryAccessors; }

    }

    public static class JakartaServletJspJstlJakartaLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJspJstlJakartaServletLibraryAccessors laccForJakartaServletJspJstlJakartaServletLibraryAccessors = new JakartaServletJspJstlJakartaServletLibraryAccessors(owner);

        public JakartaServletJspJstlJakartaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at jakarta.servlet.jsp.jstl.jakarta.servlet
         */
        public JakartaServletJspJstlJakartaServletLibraryAccessors getServlet() { return laccForJakartaServletJspJstlJakartaServletLibraryAccessors; }

    }

    public static class JakartaServletJspJstlJakartaServletLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJspJstlJakartaServletJspLibraryAccessors laccForJakartaServletJspJstlJakartaServletJspLibraryAccessors = new JakartaServletJspJstlJakartaServletJspLibraryAccessors(owner);

        public JakartaServletJspJstlJakartaServletLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at jakarta.servlet.jsp.jstl.jakarta.servlet.jsp
         */
        public JakartaServletJspJstlJakartaServletJspLibraryAccessors getJsp() { return laccForJakartaServletJspJstlJakartaServletJspLibraryAccessors; }

    }

    public static class JakartaServletJspJstlJakartaServletJspLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJspJstlJakartaServletJspJstlLibraryAccessors laccForJakartaServletJspJstlJakartaServletJspJstlLibraryAccessors = new JakartaServletJspJstlJakartaServletJspJstlLibraryAccessors(owner);

        public JakartaServletJspJstlJakartaServletJspLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at jakarta.servlet.jsp.jstl.jakarta.servlet.jsp.jstl
         */
        public JakartaServletJspJstlJakartaServletJspJstlLibraryAccessors getJstl() { return laccForJakartaServletJspJstlJakartaServletJspJstlLibraryAccessors; }

    }

    public static class JakartaServletJspJstlJakartaServletJspJstlLibraryAccessors extends SubDependencyFactory {

        public JakartaServletJspJstlJakartaServletJspJstlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for api (jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getApi() { return create("jakarta.servlet.jsp.jstl.jakarta.servlet.jsp.jstl.api"); }

    }

    public static class OrgLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheLibraryAccessors laccForOrgApacheLibraryAccessors = new OrgApacheLibraryAccessors(owner);
        private final OrgGlassfishLibraryAccessors laccForOrgGlassfishLibraryAccessors = new OrgGlassfishLibraryAccessors(owner);
        private final OrgJdbiLibraryAccessors laccForOrgJdbiLibraryAccessors = new OrgJdbiLibraryAccessors(owner);
        private final OrgJsonLibraryAccessors laccForOrgJsonLibraryAccessors = new OrgJsonLibraryAccessors(owner);
        private final OrgJunitLibraryAccessors laccForOrgJunitLibraryAccessors = new OrgJunitLibraryAccessors(owner);
        private final OrgMindrotLibraryAccessors laccForOrgMindrotLibraryAccessors = new OrgMindrotLibraryAccessors(owner);

        public OrgLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.apache
         */
        public OrgApacheLibraryAccessors getApache() { return laccForOrgApacheLibraryAccessors; }

        /**
         * Returns the group of libraries at org.glassfish
         */
        public OrgGlassfishLibraryAccessors getGlassfish() { return laccForOrgGlassfishLibraryAccessors; }

        /**
         * Returns the group of libraries at org.jdbi
         */
        public OrgJdbiLibraryAccessors getJdbi() { return laccForOrgJdbiLibraryAccessors; }

        /**
         * Returns the group of libraries at org.json
         */
        public OrgJsonLibraryAccessors getJson() { return laccForOrgJsonLibraryAccessors; }

        /**
         * Returns the group of libraries at org.junit
         */
        public OrgJunitLibraryAccessors getJunit() { return laccForOrgJunitLibraryAccessors; }

        /**
         * Returns the group of libraries at org.mindrot
         */
        public OrgMindrotLibraryAccessors getMindrot() { return laccForOrgMindrotLibraryAccessors; }

    }

    public static class OrgApacheLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheCassandraLibraryAccessors laccForOrgApacheCassandraLibraryAccessors = new OrgApacheCassandraLibraryAccessors(owner);

        public OrgApacheLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.apache.cassandra
         */
        public OrgApacheCassandraLibraryAccessors getCassandra() { return laccForOrgApacheCassandraLibraryAccessors; }

    }

    public static class OrgApacheCassandraLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheCassandraCassandraLibraryAccessors laccForOrgApacheCassandraCassandraLibraryAccessors = new OrgApacheCassandraCassandraLibraryAccessors(owner);

        public OrgApacheCassandraLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.apache.cassandra.cassandra
         */
        public OrgApacheCassandraCassandraLibraryAccessors getCassandra() { return laccForOrgApacheCassandraCassandraLibraryAccessors; }

    }

    public static class OrgApacheCassandraCassandraLibraryAccessors extends SubDependencyFactory {

        public OrgApacheCassandraCassandraLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for all (org.apache.cassandra:cassandra-all)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAll() { return create("org.apache.cassandra.cassandra.all"); }

    }

    public static class OrgGlassfishLibraryAccessors extends SubDependencyFactory {
        private final OrgGlassfishWebLibraryAccessors laccForOrgGlassfishWebLibraryAccessors = new OrgGlassfishWebLibraryAccessors(owner);

        public OrgGlassfishLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.glassfish.web
         */
        public OrgGlassfishWebLibraryAccessors getWeb() { return laccForOrgGlassfishWebLibraryAccessors; }

    }

    public static class OrgGlassfishWebLibraryAccessors extends SubDependencyFactory {
        private final OrgGlassfishWebJakartaLibraryAccessors laccForOrgGlassfishWebJakartaLibraryAccessors = new OrgGlassfishWebJakartaLibraryAccessors(owner);

        public OrgGlassfishWebLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.glassfish.web.jakarta
         */
        public OrgGlassfishWebJakartaLibraryAccessors getJakarta() { return laccForOrgGlassfishWebJakartaLibraryAccessors; }

    }

    public static class OrgGlassfishWebJakartaLibraryAccessors extends SubDependencyFactory {
        private final OrgGlassfishWebJakartaServletLibraryAccessors laccForOrgGlassfishWebJakartaServletLibraryAccessors = new OrgGlassfishWebJakartaServletLibraryAccessors(owner);

        public OrgGlassfishWebJakartaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.glassfish.web.jakarta.servlet
         */
        public OrgGlassfishWebJakartaServletLibraryAccessors getServlet() { return laccForOrgGlassfishWebJakartaServletLibraryAccessors; }

    }

    public static class OrgGlassfishWebJakartaServletLibraryAccessors extends SubDependencyFactory {
        private final OrgGlassfishWebJakartaServletJspLibraryAccessors laccForOrgGlassfishWebJakartaServletJspLibraryAccessors = new OrgGlassfishWebJakartaServletJspLibraryAccessors(owner);

        public OrgGlassfishWebJakartaServletLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.glassfish.web.jakarta.servlet.jsp
         */
        public OrgGlassfishWebJakartaServletJspLibraryAccessors getJsp() { return laccForOrgGlassfishWebJakartaServletJspLibraryAccessors; }

    }

    public static class OrgGlassfishWebJakartaServletJspLibraryAccessors extends SubDependencyFactory {

        public OrgGlassfishWebJakartaServletJspLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for jstl (org.glassfish.web:jakarta.servlet.jsp.jstl)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJstl() { return create("org.glassfish.web.jakarta.servlet.jsp.jstl"); }

    }

    public static class OrgJdbiLibraryAccessors extends SubDependencyFactory {
        private final OrgJdbiJdbi3LibraryAccessors laccForOrgJdbiJdbi3LibraryAccessors = new OrgJdbiJdbi3LibraryAccessors(owner);

        public OrgJdbiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.jdbi.jdbi3
         */
        public OrgJdbiJdbi3LibraryAccessors getJdbi3() { return laccForOrgJdbiJdbi3LibraryAccessors; }

    }

    public static class OrgJdbiJdbi3LibraryAccessors extends SubDependencyFactory {

        public OrgJdbiJdbi3LibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (org.jdbi:jdbi3-core)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("org.jdbi.jdbi3.core"); }

    }

    public static class OrgJsonLibraryAccessors extends SubDependencyFactory {

        public OrgJsonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for json (org.json:json)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJson() { return create("org.json.json"); }

    }

    public static class OrgJunitLibraryAccessors extends SubDependencyFactory {
        private final OrgJunitJupiterLibraryAccessors laccForOrgJunitJupiterLibraryAccessors = new OrgJunitJupiterLibraryAccessors(owner);

        public OrgJunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.junit.jupiter
         */
        public OrgJunitJupiterLibraryAccessors getJupiter() { return laccForOrgJunitJupiterLibraryAccessors; }

    }

    public static class OrgJunitJupiterLibraryAccessors extends SubDependencyFactory {
        private final OrgJunitJupiterJunitLibraryAccessors laccForOrgJunitJupiterJunitLibraryAccessors = new OrgJunitJupiterJunitLibraryAccessors(owner);

        public OrgJunitJupiterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.junit.jupiter.junit
         */
        public OrgJunitJupiterJunitLibraryAccessors getJunit() { return laccForOrgJunitJupiterJunitLibraryAccessors; }

    }

    public static class OrgJunitJupiterJunitLibraryAccessors extends SubDependencyFactory {
        private final OrgJunitJupiterJunitJupiterLibraryAccessors laccForOrgJunitJupiterJunitJupiterLibraryAccessors = new OrgJunitJupiterJunitJupiterLibraryAccessors(owner);

        public OrgJunitJupiterJunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.junit.jupiter.junit.jupiter
         */
        public OrgJunitJupiterJunitJupiterLibraryAccessors getJupiter() { return laccForOrgJunitJupiterJunitJupiterLibraryAccessors; }

    }

    public static class OrgJunitJupiterJunitJupiterLibraryAccessors extends SubDependencyFactory {

        public OrgJunitJupiterJunitJupiterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for api (org.junit.jupiter:junit-jupiter-api)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getApi() { return create("org.junit.jupiter.junit.jupiter.api"); }

            /**
             * Creates a dependency provider for engine (org.junit.jupiter:junit-jupiter-engine)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getEngine() { return create("org.junit.jupiter.junit.jupiter.engine"); }

    }

    public static class OrgMindrotLibraryAccessors extends SubDependencyFactory {

        public OrgMindrotLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for jbcrypt (org.mindrot:jbcrypt)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJbcrypt() { return create("org.mindrot.jbcrypt"); }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final ChVersionAccessors vaccForChVersionAccessors = new ChVersionAccessors(providers, config);
        private final ComVersionAccessors vaccForComVersionAccessors = new ComVersionAccessors(providers, config);
        private final CommonsVersionAccessors vaccForCommonsVersionAccessors = new CommonsVersionAccessors(providers, config);
        private final JakartaVersionAccessors vaccForJakartaVersionAccessors = new JakartaVersionAccessors(providers, config);
        private final OrgVersionAccessors vaccForOrgVersionAccessors = new OrgVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.ch
         */
        public ChVersionAccessors getCh() { return vaccForChVersionAccessors; }

        /**
         * Returns the group of versions at versions.com
         */
        public ComVersionAccessors getCom() { return vaccForComVersionAccessors; }

        /**
         * Returns the group of versions at versions.commons
         */
        public CommonsVersionAccessors getCommons() { return vaccForCommonsVersionAccessors; }

        /**
         * Returns the group of versions at versions.jakarta
         */
        public JakartaVersionAccessors getJakarta() { return vaccForJakartaVersionAccessors; }

        /**
         * Returns the group of versions at versions.org
         */
        public OrgVersionAccessors getOrg() { return vaccForOrgVersionAccessors; }

    }

    public static class ChVersionAccessors extends VersionFactory  {

        private final ChQosVersionAccessors vaccForChQosVersionAccessors = new ChQosVersionAccessors(providers, config);
        public ChVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.ch.qos
         */
        public ChQosVersionAccessors getQos() { return vaccForChQosVersionAccessors; }

    }

    public static class ChQosVersionAccessors extends VersionFactory  {

        private final ChQosLogbackVersionAccessors vaccForChQosLogbackVersionAccessors = new ChQosLogbackVersionAccessors(providers, config);
        public ChQosVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.ch.qos.logback
         */
        public ChQosLogbackVersionAccessors getLogback() { return vaccForChQosLogbackVersionAccessors; }

    }

    public static class ChQosLogbackVersionAccessors extends VersionFactory  {

        private final ChQosLogbackLogbackVersionAccessors vaccForChQosLogbackLogbackVersionAccessors = new ChQosLogbackLogbackVersionAccessors(providers, config);
        public ChQosLogbackVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.ch.qos.logback.logback
         */
        public ChQosLogbackLogbackVersionAccessors getLogback() { return vaccForChQosLogbackLogbackVersionAccessors; }

    }

    public static class ChQosLogbackLogbackVersionAccessors extends VersionFactory  {

        public ChQosLogbackLogbackVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: ch.qos.logback.logback.classic (1.4.11)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getClassic() { return getVersion("ch.qos.logback.logback.classic"); }

    }

    public static class ComVersionAccessors extends VersionFactory  {

        private final ComCloudinaryVersionAccessors vaccForComCloudinaryVersionAccessors = new ComCloudinaryVersionAccessors(providers, config);
        private final ComFasterxmlVersionAccessors vaccForComFasterxmlVersionAccessors = new ComFasterxmlVersionAccessors(providers, config);
        private final ComGoogleVersionAccessors vaccForComGoogleVersionAccessors = new ComGoogleVersionAccessors(providers, config);
        private final ComMysqlVersionAccessors vaccForComMysqlVersionAccessors = new ComMysqlVersionAccessors(providers, config);
        private final ComSunVersionAccessors vaccForComSunVersionAccessors = new ComSunVersionAccessors(providers, config);
        public ComVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.cloudinary
         */
        public ComCloudinaryVersionAccessors getCloudinary() { return vaccForComCloudinaryVersionAccessors; }

        /**
         * Returns the group of versions at versions.com.fasterxml
         */
        public ComFasterxmlVersionAccessors getFasterxml() { return vaccForComFasterxmlVersionAccessors; }

        /**
         * Returns the group of versions at versions.com.google
         */
        public ComGoogleVersionAccessors getGoogle() { return vaccForComGoogleVersionAccessors; }

        /**
         * Returns the group of versions at versions.com.mysql
         */
        public ComMysqlVersionAccessors getMysql() { return vaccForComMysqlVersionAccessors; }

        /**
         * Returns the group of versions at versions.com.sun
         */
        public ComSunVersionAccessors getSun() { return vaccForComSunVersionAccessors; }

    }

    public static class ComCloudinaryVersionAccessors extends VersionFactory  {

        private final ComCloudinaryCloudinaryVersionAccessors vaccForComCloudinaryCloudinaryVersionAccessors = new ComCloudinaryCloudinaryVersionAccessors(providers, config);
        public ComCloudinaryVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.cloudinary.cloudinary
         */
        public ComCloudinaryCloudinaryVersionAccessors getCloudinary() { return vaccForComCloudinaryCloudinaryVersionAccessors; }

    }

    public static class ComCloudinaryCloudinaryVersionAccessors extends VersionFactory  {

        public ComCloudinaryCloudinaryVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: com.cloudinary.cloudinary.http44 (1.32.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getHttp44() { return getVersion("com.cloudinary.cloudinary.http44"); }

    }

    public static class ComFasterxmlVersionAccessors extends VersionFactory  {

        private final ComFasterxmlJacksonVersionAccessors vaccForComFasterxmlJacksonVersionAccessors = new ComFasterxmlJacksonVersionAccessors(providers, config);
        public ComFasterxmlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.fasterxml.jackson
         */
        public ComFasterxmlJacksonVersionAccessors getJackson() { return vaccForComFasterxmlJacksonVersionAccessors; }

    }

    public static class ComFasterxmlJacksonVersionAccessors extends VersionFactory  {

        private final ComFasterxmlJacksonCoreVersionAccessors vaccForComFasterxmlJacksonCoreVersionAccessors = new ComFasterxmlJacksonCoreVersionAccessors(providers, config);
        public ComFasterxmlJacksonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.fasterxml.jackson.core
         */
        public ComFasterxmlJacksonCoreVersionAccessors getCore() { return vaccForComFasterxmlJacksonCoreVersionAccessors; }

    }

    public static class ComFasterxmlJacksonCoreVersionAccessors extends VersionFactory  {

        private final ComFasterxmlJacksonCoreJacksonVersionAccessors vaccForComFasterxmlJacksonCoreJacksonVersionAccessors = new ComFasterxmlJacksonCoreJacksonVersionAccessors(providers, config);
        public ComFasterxmlJacksonCoreVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.fasterxml.jackson.core.jackson
         */
        public ComFasterxmlJacksonCoreJacksonVersionAccessors getJackson() { return vaccForComFasterxmlJacksonCoreJacksonVersionAccessors; }

    }

    public static class ComFasterxmlJacksonCoreJacksonVersionAccessors extends VersionFactory  {

        public ComFasterxmlJacksonCoreJacksonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: com.fasterxml.jackson.core.jackson.databind (2.15.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getDatabind() { return getVersion("com.fasterxml.jackson.core.jackson.databind"); }

    }

    public static class ComGoogleVersionAccessors extends VersionFactory  {

        private final ComGoogleApiVersionAccessors vaccForComGoogleApiVersionAccessors = new ComGoogleApiVersionAccessors(providers, config);
        private final ComGoogleApisVersionAccessors vaccForComGoogleApisVersionAccessors = new ComGoogleApisVersionAccessors(providers, config);
        private final ComGoogleCodeVersionAccessors vaccForComGoogleCodeVersionAccessors = new ComGoogleCodeVersionAccessors(providers, config);
        private final ComGoogleGuavaVersionAccessors vaccForComGoogleGuavaVersionAccessors = new ComGoogleGuavaVersionAccessors(providers, config);
        private final ComGoogleHttpVersionAccessors vaccForComGoogleHttpVersionAccessors = new ComGoogleHttpVersionAccessors(providers, config);
        private final ComGoogleOauthVersionAccessors vaccForComGoogleOauthVersionAccessors = new ComGoogleOauthVersionAccessors(providers, config);
        public ComGoogleVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.api
         */
        public ComGoogleApiVersionAccessors getApi() { return vaccForComGoogleApiVersionAccessors; }

        /**
         * Returns the group of versions at versions.com.google.apis
         */
        public ComGoogleApisVersionAccessors getApis() { return vaccForComGoogleApisVersionAccessors; }

        /**
         * Returns the group of versions at versions.com.google.code
         */
        public ComGoogleCodeVersionAccessors getCode() { return vaccForComGoogleCodeVersionAccessors; }

        /**
         * Returns the group of versions at versions.com.google.guava
         */
        public ComGoogleGuavaVersionAccessors getGuava() { return vaccForComGoogleGuavaVersionAccessors; }

        /**
         * Returns the group of versions at versions.com.google.http
         */
        public ComGoogleHttpVersionAccessors getHttp() { return vaccForComGoogleHttpVersionAccessors; }

        /**
         * Returns the group of versions at versions.com.google.oauth
         */
        public ComGoogleOauthVersionAccessors getOauth() { return vaccForComGoogleOauthVersionAccessors; }

    }

    public static class ComGoogleApiVersionAccessors extends VersionFactory  {

        private final ComGoogleApiClientVersionAccessors vaccForComGoogleApiClientVersionAccessors = new ComGoogleApiClientVersionAccessors(providers, config);
        public ComGoogleApiVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.api.client
         */
        public ComGoogleApiClientVersionAccessors getClient() { return vaccForComGoogleApiClientVersionAccessors; }

    }

    public static class ComGoogleApiClientVersionAccessors extends VersionFactory  {

        private final ComGoogleApiClientGoogleVersionAccessors vaccForComGoogleApiClientGoogleVersionAccessors = new ComGoogleApiClientGoogleVersionAccessors(providers, config);
        public ComGoogleApiClientVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.api.client.google
         */
        public ComGoogleApiClientGoogleVersionAccessors getGoogle() { return vaccForComGoogleApiClientGoogleVersionAccessors; }

    }

    public static class ComGoogleApiClientGoogleVersionAccessors extends VersionFactory  {

        private final ComGoogleApiClientGoogleApiVersionAccessors vaccForComGoogleApiClientGoogleApiVersionAccessors = new ComGoogleApiClientGoogleApiVersionAccessors(providers, config);
        public ComGoogleApiClientGoogleVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.api.client.google.api
         */
        public ComGoogleApiClientGoogleApiVersionAccessors getApi() { return vaccForComGoogleApiClientGoogleApiVersionAccessors; }

    }

    public static class ComGoogleApiClientGoogleApiVersionAccessors extends VersionFactory  {

        public ComGoogleApiClientGoogleApiVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: com.google.api.client.google.api.client (1.33.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getClient() { return getVersion("com.google.api.client.google.api.client"); }

    }

    public static class ComGoogleApisVersionAccessors extends VersionFactory  {

        private final ComGoogleApisGoogleVersionAccessors vaccForComGoogleApisGoogleVersionAccessors = new ComGoogleApisGoogleVersionAccessors(providers, config);
        public ComGoogleApisVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.apis.google
         */
        public ComGoogleApisGoogleVersionAccessors getGoogle() { return vaccForComGoogleApisGoogleVersionAccessors; }

    }

    public static class ComGoogleApisGoogleVersionAccessors extends VersionFactory  {

        private final ComGoogleApisGoogleApiVersionAccessors vaccForComGoogleApisGoogleApiVersionAccessors = new ComGoogleApisGoogleApiVersionAccessors(providers, config);
        public ComGoogleApisGoogleVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.apis.google.api
         */
        public ComGoogleApisGoogleApiVersionAccessors getApi() { return vaccForComGoogleApisGoogleApiVersionAccessors; }

    }

    public static class ComGoogleApisGoogleApiVersionAccessors extends VersionFactory  {

        private final ComGoogleApisGoogleApiServicesVersionAccessors vaccForComGoogleApisGoogleApiServicesVersionAccessors = new ComGoogleApisGoogleApiServicesVersionAccessors(providers, config);
        public ComGoogleApisGoogleApiVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.apis.google.api.services
         */
        public ComGoogleApisGoogleApiServicesVersionAccessors getServices() { return vaccForComGoogleApisGoogleApiServicesVersionAccessors; }

    }

    public static class ComGoogleApisGoogleApiServicesVersionAccessors extends VersionFactory  {

        public ComGoogleApisGoogleApiServicesVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: com.google.apis.google.api.services.oauth2 (v2-rev157-1.25.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getOauth2() { return getVersion("com.google.apis.google.api.services.oauth2"); }

    }

    public static class ComGoogleCodeVersionAccessors extends VersionFactory  {

        private final ComGoogleCodeGsonVersionAccessors vaccForComGoogleCodeGsonVersionAccessors = new ComGoogleCodeGsonVersionAccessors(providers, config);
        public ComGoogleCodeVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.code.gson
         */
        public ComGoogleCodeGsonVersionAccessors getGson() { return vaccForComGoogleCodeGsonVersionAccessors; }

    }

    public static class ComGoogleCodeGsonVersionAccessors extends VersionFactory  {

        public ComGoogleCodeGsonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: com.google.code.gson.gson (2.10.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGson() { return getVersion("com.google.code.gson.gson"); }

    }

    public static class ComGoogleGuavaVersionAccessors extends VersionFactory  {

        public ComGoogleGuavaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: com.google.guava.guava (32.0.0-jre)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGuava() { return getVersion("com.google.guava.guava"); }

    }

    public static class ComGoogleHttpVersionAccessors extends VersionFactory  {

        private final ComGoogleHttpClientVersionAccessors vaccForComGoogleHttpClientVersionAccessors = new ComGoogleHttpClientVersionAccessors(providers, config);
        public ComGoogleHttpVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.http.client
         */
        public ComGoogleHttpClientVersionAccessors getClient() { return vaccForComGoogleHttpClientVersionAccessors; }

    }

    public static class ComGoogleHttpClientVersionAccessors extends VersionFactory  {

        private final ComGoogleHttpClientGoogleVersionAccessors vaccForComGoogleHttpClientGoogleVersionAccessors = new ComGoogleHttpClientGoogleVersionAccessors(providers, config);
        public ComGoogleHttpClientVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.http.client.google
         */
        public ComGoogleHttpClientGoogleVersionAccessors getGoogle() { return vaccForComGoogleHttpClientGoogleVersionAccessors; }

    }

    public static class ComGoogleHttpClientGoogleVersionAccessors extends VersionFactory  {

        private final ComGoogleHttpClientGoogleHttpVersionAccessors vaccForComGoogleHttpClientGoogleHttpVersionAccessors = new ComGoogleHttpClientGoogleHttpVersionAccessors(providers, config);
        public ComGoogleHttpClientGoogleVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.http.client.google.http
         */
        public ComGoogleHttpClientGoogleHttpVersionAccessors getHttp() { return vaccForComGoogleHttpClientGoogleHttpVersionAccessors; }

    }

    public static class ComGoogleHttpClientGoogleHttpVersionAccessors extends VersionFactory  {

        private final ComGoogleHttpClientGoogleHttpClientVersionAccessors vaccForComGoogleHttpClientGoogleHttpClientVersionAccessors = new ComGoogleHttpClientGoogleHttpClientVersionAccessors(providers, config);
        public ComGoogleHttpClientGoogleHttpVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.http.client.google.http.client
         */
        public ComGoogleHttpClientGoogleHttpClientVersionAccessors getClient() { return vaccForComGoogleHttpClientGoogleHttpClientVersionAccessors; }

    }

    public static class ComGoogleHttpClientGoogleHttpClientVersionAccessors extends VersionFactory  {

        public ComGoogleHttpClientGoogleHttpClientVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: com.google.http.client.google.http.client.jackson2 (1.41.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJackson2() { return getVersion("com.google.http.client.google.http.client.jackson2"); }

    }

    public static class ComGoogleOauthVersionAccessors extends VersionFactory  {

        private final ComGoogleOauthClientVersionAccessors vaccForComGoogleOauthClientVersionAccessors = new ComGoogleOauthClientVersionAccessors(providers, config);
        public ComGoogleOauthVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.oauth.client
         */
        public ComGoogleOauthClientVersionAccessors getClient() { return vaccForComGoogleOauthClientVersionAccessors; }

    }

    public static class ComGoogleOauthClientVersionAccessors extends VersionFactory  {

        private final ComGoogleOauthClientGoogleVersionAccessors vaccForComGoogleOauthClientGoogleVersionAccessors = new ComGoogleOauthClientGoogleVersionAccessors(providers, config);
        public ComGoogleOauthClientVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.oauth.client.google
         */
        public ComGoogleOauthClientGoogleVersionAccessors getGoogle() { return vaccForComGoogleOauthClientGoogleVersionAccessors; }

    }

    public static class ComGoogleOauthClientGoogleVersionAccessors extends VersionFactory  {

        private final ComGoogleOauthClientGoogleOauthVersionAccessors vaccForComGoogleOauthClientGoogleOauthVersionAccessors = new ComGoogleOauthClientGoogleOauthVersionAccessors(providers, config);
        public ComGoogleOauthClientGoogleVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.oauth.client.google.oauth
         */
        public ComGoogleOauthClientGoogleOauthVersionAccessors getOauth() { return vaccForComGoogleOauthClientGoogleOauthVersionAccessors; }

    }

    public static class ComGoogleOauthClientGoogleOauthVersionAccessors extends VersionFactory  {

        private final ComGoogleOauthClientGoogleOauthClientVersionAccessors vaccForComGoogleOauthClientGoogleOauthClientVersionAccessors = new ComGoogleOauthClientGoogleOauthClientVersionAccessors(providers, config);
        public ComGoogleOauthClientGoogleOauthVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.google.oauth.client.google.oauth.client
         */
        public ComGoogleOauthClientGoogleOauthClientVersionAccessors getClient() { return vaccForComGoogleOauthClientGoogleOauthClientVersionAccessors; }

    }

    public static class ComGoogleOauthClientGoogleOauthClientVersionAccessors extends VersionFactory  {

        public ComGoogleOauthClientGoogleOauthClientVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: com.google.oauth.client.google.oauth.client.jetty (1.34.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJetty() { return getVersion("com.google.oauth.client.google.oauth.client.jetty"); }

    }

    public static class ComMysqlVersionAccessors extends VersionFactory  {

        private final ComMysqlMysqlVersionAccessors vaccForComMysqlMysqlVersionAccessors = new ComMysqlMysqlVersionAccessors(providers, config);
        public ComMysqlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.mysql.mysql
         */
        public ComMysqlMysqlVersionAccessors getMysql() { return vaccForComMysqlMysqlVersionAccessors; }

    }

    public static class ComMysqlMysqlVersionAccessors extends VersionFactory  {

        private final ComMysqlMysqlConnectorVersionAccessors vaccForComMysqlMysqlConnectorVersionAccessors = new ComMysqlMysqlConnectorVersionAccessors(providers, config);
        public ComMysqlMysqlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.mysql.mysql.connector
         */
        public ComMysqlMysqlConnectorVersionAccessors getConnector() { return vaccForComMysqlMysqlConnectorVersionAccessors; }

    }

    public static class ComMysqlMysqlConnectorVersionAccessors extends VersionFactory  {

        public ComMysqlMysqlConnectorVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: com.mysql.mysql.connector.j (8.3.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJ() { return getVersion("com.mysql.mysql.connector.j"); }

    }

    public static class ComSunVersionAccessors extends VersionFactory  {

        private final ComSunMailVersionAccessors vaccForComSunMailVersionAccessors = new ComSunMailVersionAccessors(providers, config);
        public ComSunVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.sun.mail
         */
        public ComSunMailVersionAccessors getMail() { return vaccForComSunMailVersionAccessors; }

    }

    public static class ComSunMailVersionAccessors extends VersionFactory  {

        private final ComSunMailJavaxVersionAccessors vaccForComSunMailJavaxVersionAccessors = new ComSunMailJavaxVersionAccessors(providers, config);
        public ComSunMailVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.sun.mail.javax
         */
        public ComSunMailJavaxVersionAccessors getJavax() { return vaccForComSunMailJavaxVersionAccessors; }

    }

    public static class ComSunMailJavaxVersionAccessors extends VersionFactory  {

        public ComSunMailJavaxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: com.sun.mail.javax.mail (1.6.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMail() { return getVersion("com.sun.mail.javax.mail"); }

    }

    public static class CommonsVersionAccessors extends VersionFactory  {

        private final CommonsIoVersionAccessors vaccForCommonsIoVersionAccessors = new CommonsIoVersionAccessors(providers, config);
        public CommonsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.commons.io
         */
        public CommonsIoVersionAccessors getIo() { return vaccForCommonsIoVersionAccessors; }

    }

    public static class CommonsIoVersionAccessors extends VersionFactory  {

        private final CommonsIoCommonsVersionAccessors vaccForCommonsIoCommonsVersionAccessors = new CommonsIoCommonsVersionAccessors(providers, config);
        public CommonsIoVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.commons.io.commons
         */
        public CommonsIoCommonsVersionAccessors getCommons() { return vaccForCommonsIoCommonsVersionAccessors; }

    }

    public static class CommonsIoCommonsVersionAccessors extends VersionFactory  {

        public CommonsIoCommonsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: commons.io.commons.io (2.11.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getIo() { return getVersion("commons.io.commons.io"); }

    }

    public static class JakartaVersionAccessors extends VersionFactory  {

        private final JakartaServletVersionAccessors vaccForJakartaServletVersionAccessors = new JakartaServletVersionAccessors(providers, config);
        public JakartaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.jakarta.servlet
         */
        public JakartaServletVersionAccessors getServlet() { return vaccForJakartaServletVersionAccessors; }

    }

    public static class JakartaServletVersionAccessors extends VersionFactory  {

        private final JakartaServletJakartaVersionAccessors vaccForJakartaServletJakartaVersionAccessors = new JakartaServletJakartaVersionAccessors(providers, config);
        private final JakartaServletJspVersionAccessors vaccForJakartaServletJspVersionAccessors = new JakartaServletJspVersionAccessors(providers, config);
        public JakartaServletVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.jakarta.servlet.jakarta
         */
        public JakartaServletJakartaVersionAccessors getJakarta() { return vaccForJakartaServletJakartaVersionAccessors; }

        /**
         * Returns the group of versions at versions.jakarta.servlet.jsp
         */
        public JakartaServletJspVersionAccessors getJsp() { return vaccForJakartaServletJspVersionAccessors; }

    }

    public static class JakartaServletJakartaVersionAccessors extends VersionFactory  {

        private final JakartaServletJakartaServletVersionAccessors vaccForJakartaServletJakartaServletVersionAccessors = new JakartaServletJakartaServletVersionAccessors(providers, config);
        public JakartaServletJakartaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.jakarta.servlet.jakarta.servlet
         */
        public JakartaServletJakartaServletVersionAccessors getServlet() { return vaccForJakartaServletJakartaServletVersionAccessors; }

    }

    public static class JakartaServletJakartaServletVersionAccessors extends VersionFactory  {

        public JakartaServletJakartaServletVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: jakarta.servlet.jakarta.servlet.api (6.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getApi() { return getVersion("jakarta.servlet.jakarta.servlet.api"); }

    }

    public static class JakartaServletJspVersionAccessors extends VersionFactory  {

        private final JakartaServletJspJstlVersionAccessors vaccForJakartaServletJspJstlVersionAccessors = new JakartaServletJspJstlVersionAccessors(providers, config);
        public JakartaServletJspVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.jakarta.servlet.jsp.jstl
         */
        public JakartaServletJspJstlVersionAccessors getJstl() { return vaccForJakartaServletJspJstlVersionAccessors; }

    }

    public static class JakartaServletJspJstlVersionAccessors extends VersionFactory  {

        private final JakartaServletJspJstlJakartaVersionAccessors vaccForJakartaServletJspJstlJakartaVersionAccessors = new JakartaServletJspJstlJakartaVersionAccessors(providers, config);
        public JakartaServletJspJstlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.jakarta.servlet.jsp.jstl.jakarta
         */
        public JakartaServletJspJstlJakartaVersionAccessors getJakarta() { return vaccForJakartaServletJspJstlJakartaVersionAccessors; }

    }

    public static class JakartaServletJspJstlJakartaVersionAccessors extends VersionFactory  {

        private final JakartaServletJspJstlJakartaServletVersionAccessors vaccForJakartaServletJspJstlJakartaServletVersionAccessors = new JakartaServletJspJstlJakartaServletVersionAccessors(providers, config);
        public JakartaServletJspJstlJakartaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.jakarta.servlet.jsp.jstl.jakarta.servlet
         */
        public JakartaServletJspJstlJakartaServletVersionAccessors getServlet() { return vaccForJakartaServletJspJstlJakartaServletVersionAccessors; }

    }

    public static class JakartaServletJspJstlJakartaServletVersionAccessors extends VersionFactory  {

        private final JakartaServletJspJstlJakartaServletJspVersionAccessors vaccForJakartaServletJspJstlJakartaServletJspVersionAccessors = new JakartaServletJspJstlJakartaServletJspVersionAccessors(providers, config);
        public JakartaServletJspJstlJakartaServletVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.jakarta.servlet.jsp.jstl.jakarta.servlet.jsp
         */
        public JakartaServletJspJstlJakartaServletJspVersionAccessors getJsp() { return vaccForJakartaServletJspJstlJakartaServletJspVersionAccessors; }

    }

    public static class JakartaServletJspJstlJakartaServletJspVersionAccessors extends VersionFactory  {

        private final JakartaServletJspJstlJakartaServletJspJstlVersionAccessors vaccForJakartaServletJspJstlJakartaServletJspJstlVersionAccessors = new JakartaServletJspJstlJakartaServletJspJstlVersionAccessors(providers, config);
        public JakartaServletJspJstlJakartaServletJspVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.jakarta.servlet.jsp.jstl.jakarta.servlet.jsp.jstl
         */
        public JakartaServletJspJstlJakartaServletJspJstlVersionAccessors getJstl() { return vaccForJakartaServletJspJstlJakartaServletJspJstlVersionAccessors; }

    }

    public static class JakartaServletJspJstlJakartaServletJspJstlVersionAccessors extends VersionFactory  {

        public JakartaServletJspJstlJakartaServletJspJstlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: jakarta.servlet.jsp.jstl.jakarta.servlet.jsp.jstl.api (3.0.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getApi() { return getVersion("jakarta.servlet.jsp.jstl.jakarta.servlet.jsp.jstl.api"); }

    }

    public static class OrgVersionAccessors extends VersionFactory  {

        private final OrgApacheVersionAccessors vaccForOrgApacheVersionAccessors = new OrgApacheVersionAccessors(providers, config);
        private final OrgGlassfishVersionAccessors vaccForOrgGlassfishVersionAccessors = new OrgGlassfishVersionAccessors(providers, config);
        private final OrgJdbiVersionAccessors vaccForOrgJdbiVersionAccessors = new OrgJdbiVersionAccessors(providers, config);
        private final OrgJsonVersionAccessors vaccForOrgJsonVersionAccessors = new OrgJsonVersionAccessors(providers, config);
        private final OrgJunitVersionAccessors vaccForOrgJunitVersionAccessors = new OrgJunitVersionAccessors(providers, config);
        private final OrgMindrotVersionAccessors vaccForOrgMindrotVersionAccessors = new OrgMindrotVersionAccessors(providers, config);
        public OrgVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.apache
         */
        public OrgApacheVersionAccessors getApache() { return vaccForOrgApacheVersionAccessors; }

        /**
         * Returns the group of versions at versions.org.glassfish
         */
        public OrgGlassfishVersionAccessors getGlassfish() { return vaccForOrgGlassfishVersionAccessors; }

        /**
         * Returns the group of versions at versions.org.jdbi
         */
        public OrgJdbiVersionAccessors getJdbi() { return vaccForOrgJdbiVersionAccessors; }

        /**
         * Returns the group of versions at versions.org.json
         */
        public OrgJsonVersionAccessors getJson() { return vaccForOrgJsonVersionAccessors; }

        /**
         * Returns the group of versions at versions.org.junit
         */
        public OrgJunitVersionAccessors getJunit() { return vaccForOrgJunitVersionAccessors; }

        /**
         * Returns the group of versions at versions.org.mindrot
         */
        public OrgMindrotVersionAccessors getMindrot() { return vaccForOrgMindrotVersionAccessors; }

    }

    public static class OrgApacheVersionAccessors extends VersionFactory  {

        private final OrgApacheCassandraVersionAccessors vaccForOrgApacheCassandraVersionAccessors = new OrgApacheCassandraVersionAccessors(providers, config);
        public OrgApacheVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.apache.cassandra
         */
        public OrgApacheCassandraVersionAccessors getCassandra() { return vaccForOrgApacheCassandraVersionAccessors; }

    }

    public static class OrgApacheCassandraVersionAccessors extends VersionFactory  {

        private final OrgApacheCassandraCassandraVersionAccessors vaccForOrgApacheCassandraCassandraVersionAccessors = new OrgApacheCassandraCassandraVersionAccessors(providers, config);
        public OrgApacheCassandraVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.apache.cassandra.cassandra
         */
        public OrgApacheCassandraCassandraVersionAccessors getCassandra() { return vaccForOrgApacheCassandraCassandraVersionAccessors; }

    }

    public static class OrgApacheCassandraCassandraVersionAccessors extends VersionFactory  {

        public OrgApacheCassandraCassandraVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: org.apache.cassandra.cassandra.all (0.8.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAll() { return getVersion("org.apache.cassandra.cassandra.all"); }

    }

    public static class OrgGlassfishVersionAccessors extends VersionFactory  {

        private final OrgGlassfishWebVersionAccessors vaccForOrgGlassfishWebVersionAccessors = new OrgGlassfishWebVersionAccessors(providers, config);
        public OrgGlassfishVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.glassfish.web
         */
        public OrgGlassfishWebVersionAccessors getWeb() { return vaccForOrgGlassfishWebVersionAccessors; }

    }

    public static class OrgGlassfishWebVersionAccessors extends VersionFactory  {

        private final OrgGlassfishWebJakartaVersionAccessors vaccForOrgGlassfishWebJakartaVersionAccessors = new OrgGlassfishWebJakartaVersionAccessors(providers, config);
        public OrgGlassfishWebVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.glassfish.web.jakarta
         */
        public OrgGlassfishWebJakartaVersionAccessors getJakarta() { return vaccForOrgGlassfishWebJakartaVersionAccessors; }

    }

    public static class OrgGlassfishWebJakartaVersionAccessors extends VersionFactory  {

        private final OrgGlassfishWebJakartaServletVersionAccessors vaccForOrgGlassfishWebJakartaServletVersionAccessors = new OrgGlassfishWebJakartaServletVersionAccessors(providers, config);
        public OrgGlassfishWebJakartaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.glassfish.web.jakarta.servlet
         */
        public OrgGlassfishWebJakartaServletVersionAccessors getServlet() { return vaccForOrgGlassfishWebJakartaServletVersionAccessors; }

    }

    public static class OrgGlassfishWebJakartaServletVersionAccessors extends VersionFactory  {

        private final OrgGlassfishWebJakartaServletJspVersionAccessors vaccForOrgGlassfishWebJakartaServletJspVersionAccessors = new OrgGlassfishWebJakartaServletJspVersionAccessors(providers, config);
        public OrgGlassfishWebJakartaServletVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.glassfish.web.jakarta.servlet.jsp
         */
        public OrgGlassfishWebJakartaServletJspVersionAccessors getJsp() { return vaccForOrgGlassfishWebJakartaServletJspVersionAccessors; }

    }

    public static class OrgGlassfishWebJakartaServletJspVersionAccessors extends VersionFactory  {

        public OrgGlassfishWebJakartaServletJspVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: org.glassfish.web.jakarta.servlet.jsp.jstl (3.0.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJstl() { return getVersion("org.glassfish.web.jakarta.servlet.jsp.jstl"); }

    }

    public static class OrgJdbiVersionAccessors extends VersionFactory  {

        private final OrgJdbiJdbi3VersionAccessors vaccForOrgJdbiJdbi3VersionAccessors = new OrgJdbiJdbi3VersionAccessors(providers, config);
        public OrgJdbiVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.jdbi.jdbi3
         */
        public OrgJdbiJdbi3VersionAccessors getJdbi3() { return vaccForOrgJdbiJdbi3VersionAccessors; }

    }

    public static class OrgJdbiJdbi3VersionAccessors extends VersionFactory  {

        public OrgJdbiJdbi3VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: org.jdbi.jdbi3.core (3.47.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCore() { return getVersion("org.jdbi.jdbi3.core"); }

    }

    public static class OrgJsonVersionAccessors extends VersionFactory  {

        public OrgJsonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: org.json.json (20210307)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJson() { return getVersion("org.json.json"); }

    }

    public static class OrgJunitVersionAccessors extends VersionFactory  {

        private final OrgJunitJupiterVersionAccessors vaccForOrgJunitJupiterVersionAccessors = new OrgJunitJupiterVersionAccessors(providers, config);
        public OrgJunitVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.junit.jupiter
         */
        public OrgJunitJupiterVersionAccessors getJupiter() { return vaccForOrgJunitJupiterVersionAccessors; }

    }

    public static class OrgJunitJupiterVersionAccessors extends VersionFactory  {

        private final OrgJunitJupiterJunitVersionAccessors vaccForOrgJunitJupiterJunitVersionAccessors = new OrgJunitJupiterJunitVersionAccessors(providers, config);
        public OrgJunitJupiterVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.junit.jupiter.junit
         */
        public OrgJunitJupiterJunitVersionAccessors getJunit() { return vaccForOrgJunitJupiterJunitVersionAccessors; }

    }

    public static class OrgJunitJupiterJunitVersionAccessors extends VersionFactory  {

        private final OrgJunitJupiterJunitJupiterVersionAccessors vaccForOrgJunitJupiterJunitJupiterVersionAccessors = new OrgJunitJupiterJunitJupiterVersionAccessors(providers, config);
        public OrgJunitJupiterJunitVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.junit.jupiter.junit.jupiter
         */
        public OrgJunitJupiterJunitJupiterVersionAccessors getJupiter() { return vaccForOrgJunitJupiterJunitJupiterVersionAccessors; }

    }

    public static class OrgJunitJupiterJunitJupiterVersionAccessors extends VersionFactory  {

        public OrgJunitJupiterJunitJupiterVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: org.junit.jupiter.junit.jupiter.api (5.11.0-M2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getApi() { return getVersion("org.junit.jupiter.junit.jupiter.api"); }

            /**
             * Returns the version associated to this alias: org.junit.jupiter.junit.jupiter.engine (5.11.0-M2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getEngine() { return getVersion("org.junit.jupiter.junit.jupiter.engine"); }

    }

    public static class OrgMindrotVersionAccessors extends VersionFactory  {

        public OrgMindrotVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: org.mindrot.jbcrypt (0.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJbcrypt() { return getVersion("org.mindrot.jbcrypt"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
