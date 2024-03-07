/**
 * get the sha256 value of the input
 * @param input
 * @returns {Promise<string>}
 */

async function sha256(input) {
    return crypto.subtle
        .digest('SHA-256', new TextEncoder('utf-8').encode(input))
        .then(h => {
            let hexes = [],
                view = new DataView(h);
            for (let i = 0; i < view.byteLength; i += 4)
                hexes.push(('00000000' + view.getUint32(i).toString(16)).slice(-8));
            return hexes.join('');
        });
}


/**
 * 参数长度验证
 *
 * @param obj
 * @param length
 * @returns {boolean}
 */

function validLength(obj, length) {
    return obj.trim().length < length;

}

/**
 * 获取url参数
 * @param name
 * @returns {null|string}
 */

function getQueryString(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    let r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return null;
}