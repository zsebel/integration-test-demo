function fn() {
    karate.configure('connectTimeout', 5000);
    karate.configure('readTimeout', 5000);
    const port = karate.properties['local.server.port'];
    karate.log('local.server.port is:', port)
    const config = {
        baseUrl: 'http://localhost:' + port,
    };
    return config;
}