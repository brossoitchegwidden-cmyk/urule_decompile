const assert = require('assert');

process.env.BABEL_DISABLE_CACHE = '1';
require('babel-register')({presets: ['env']});

const utils = require('../src/Utils.js');

assert.strictEqual(
    utils.getParameter('file.name', '?file.name=rules%2Fdemo.xml&label=hello+world'),
    'rules/demo.xml'
);
assert.strictEqual(utils.getParameter('label', '?label=hello+world'), 'hello world');
assert.strictEqual(utils.getParameter('missing', '?label=value'), null);
assert.doesNotThrow(() => utils.getParameter('value', '?value=%E0%A4%A'));

const localDate = new Date(2024, 0, 2, 3, 4, 5);
assert.strictEqual(utils.formatDate(localDate, 'yyyy-MM-dd HH:mm:ss'), '2024-01-02 03:04:05');
assert.strictEqual(utils.formatDate('already formatted', 'yyyy-MM-dd'), 'already formatted');
assert.strictEqual(utils.formatDate(null, 'yyyy-MM-dd'), '');
assert.strictEqual(utils.formatDate(new Date('invalid'), 'yyyy-MM-dd'), '');

assert.strictEqual(
    utils.escapeXmlAttribute(`A&B <tag> "quoted" 'single'`),
    'A&amp;B &lt;tag&gt; &quot;quoted&quot; &apos;single&apos;'
);

global.window = {_frameStyles: {projectIcon: 'custom-project'}};
const styles = require('../src/Styles.js').default;
assert.strictEqual(styles.frameStyle.getProjectIcon(), 'custom-project');
assert.strictEqual(styles.frameStyle.getRuleIcon(), 'rf rf-rule');
assert.strictEqual(styles.frameStyle.getFolderIcon(), 'rf rf-folder');

console.log('Frontend utility tests passed.');
