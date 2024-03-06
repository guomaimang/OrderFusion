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