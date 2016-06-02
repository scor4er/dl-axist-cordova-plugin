	/*global cordova, module*/

    module.exports = {
        start: function () {
            cordova.exec(null, null, 'DlAxistScanner', 'start', []);
        },
        registerForBarcode: function (callback) {
            cordova.exec(callback, null, 'DlAxistScanner', 'register', []);
        },
        trigger: function (barcode) {
            cordova.exec(null, null, 'DlAxistScanner', 'trigger', [barcode]);
        }
    };