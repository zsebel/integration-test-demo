function fn() {
    const port = karate.properties['karate.port'];
    karate.log('karate.port is:', port)
    const config = {
        baseUrl: 'http://localhost:' + port,
        coinbaseBaseUrl: karate.properties['coinbase.baseUrl']
    };
    return config;
}