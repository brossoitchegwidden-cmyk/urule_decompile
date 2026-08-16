/**
 * Legacy URule Console React source build.
 *
 * The restored source tree predates the modern 4.3.0 runtime frontend. Its
 * bundles are emitted to dist/legacy for reference validation only. Production
 * assets under urule-console-pro are restored byte-for-byte from the original
 * JAR by scripts/restore_console_resources_from_original.ps1.
 */
const path = require('path');

module.exports = {
    mode: 'development',
    entry: {
        frame: './src/frame/index.jsx',
        variableEditor: './src/variable/index.jsx',
        constantEditor: './src/constant/index.jsx',
        parameterEditor: './src/parameter/index.jsx',
        actionEditor: './src/action/index.jsx',
        packageEditor: './src/package/index.jsx',
        flowDesigner: './src/flow/index.jsx',
        ruleSetEditor: './src/editor/urule/index.jsx',
        decisionTableEditor: './src/editor/decisiontable/index.jsx',
        scriptDecisionTableEditor: './src/editor/scriptdecisiontable/index.jsx',
        decisionTreeEditor: './src/editor/decisiontree/index.jsx',
        clientConfigEditor: './src/client/index.jsx',
        ulEditor: './src/editor/ul/index.jsx',
        scoreCardTable: './src/scorecard/index.jsx',
        permissionConfigEditor: './src/permission/index.jsx'
    },
    output: {
        // This source tree is the legacy React reference implementation. Keep its
        // output isolated from the modern 4.3.0 runtime assets restored from the JAR.
        path: path.resolve(__dirname, 'dist/legacy'),
        filename: '[name].bundle.js'
    },
    module: {
        rules: [
            {
                test: /\.(jsx|js)?$/,
                exclude: /node_modules/,
                loader: 'babel-loader',
                options: { presets: ['react', 'env'] }
            },
            { test: /\.css$/, use: [{ loader: 'style-loader' }, { loader: 'css-loader' }] },
            {
                test: /\.(eot|woff|woff2|ttf|svg|png|jpg)$/,
                use: [{ loader: 'url-loader', options: { limit: 10000000 } }]
            }
        ]
    }
};
