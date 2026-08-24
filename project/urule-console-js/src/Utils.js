const DEFAULT_REQUEST_ERROR_MESSAGE = '服务端错误，操作失败!';
const UNAUTHORIZED_MESSAGE = '权限不足，不能进行此操作.';

function decodeQueryComponent(value) {
    const normalizedValue = value.replace(/\+/g, ' ');
    try {
        return decodeURIComponent(normalizedValue);
    } catch (error) {
        // Keep malformed legacy URLs usable instead of failing page startup.
        return normalizedValue;
    }
}

/**
 * Reads one query-string value.
 *
 * The optional search argument keeps this function deterministic in tests and
 * lets callers parse a URL other than the current browser location.
 */
export function getParameter(name, search) {
    if (!name) {
        return null;
    }

    const query = typeof search === 'string' ? search : window.location.search;
    const queryWithoutPrefix = query.charAt(0) === '?' ? query.substring(1) : query;
    const parameters = queryWithoutPrefix.split('&');

    for (let index = 0; index < parameters.length; index++) {
        const parameter = parameters[index];
        if (!parameter) {
            continue;
        }

        const separatorIndex = parameter.indexOf('=');
        const rawName = separatorIndex === -1 ? parameter : parameter.substring(0, separatorIndex);
        if (decodeQueryComponent(rawName) !== name) {
            continue;
        }

        const rawValue = separatorIndex === -1 ? '' : parameter.substring(separatorIndex + 1);
        return decodeQueryComponent(rawValue);
    }

    return null;
}

export function showRequestError(request, fallbackMessage = DEFAULT_REQUEST_ERROR_MESSAGE) {
    const message = request && request.status === 401 ? UNAUTHORIZED_MESSAGE : fallbackMessage;
    if (typeof bootbox !== 'undefined' && bootbox.alert) {
        bootbox.alert(message);
    } else {
        alert(message);
    }
}

/**
 * Saves form-style data and returns the jqXHR so callers may attach additional
 * completion handlers when needed.
 */
export function ajaxSave(url, parameters, callback = function () {}) {
    return $.ajax({
        type: 'POST',
        url,
        data: parameters,
        success: callback,
        error: showRequestError
    });
}

export function formatDate(date, format) {
    if (date === null || typeof date === 'undefined') {
        return '';
    }
    if (typeof date === 'string') {
        return date;
    }

    const value = typeof date === 'number' ? new Date(date) : date;
    if (!(value instanceof Date) || isNaN(value.getTime())) {
        return '';
    }

    let result = format || 'yyyy-MM-dd HH:mm:ss';
    const yearMatch = /(y+)/.exec(result);
    if (yearMatch) {
        const year = String(value.getFullYear());
        result = result.replace(yearMatch[0], year.substring(4 - yearMatch[0].length));
    }

    const dateParts = {
        'M+': value.getMonth() + 1,
        'd+': value.getDate(),
        'H+': value.getHours(),
        'm+': value.getMinutes(),
        's+': value.getSeconds()
    };
    Object.keys(dateParts).forEach(token => {
        const match = new RegExp(`(${token})`).exec(result);
        if (!match) {
            return;
        }
        const rawValue = String(dateParts[token]);
        const formattedValue = match[0].length === 1 ? rawValue : `00${rawValue}`.slice(-2);
        result = result.replace(match[0], formattedValue);
    });
    return result;
}

/** Escapes a value before it is placed inside an XML attribute. */
export function escapeXmlAttribute(value) {
    if (value === null || typeof value === 'undefined') {
        return '';
    }
    return String(value)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&apos;');
}
