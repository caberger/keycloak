const path = require("path")
const webpack = require("webpack")
const HtmlWebpackPlugin = require("html-webpack-plugin")
const CopyWebpackPlugin = require("copy-webpack-plugin")

const isProduction = process.env.NODE_ENV == 'production'

const config = {
    entry: './src/index.ts',
    output: {
        clean: true,
        path: path.resolve(__dirname, 'target'),
    },
    devtool: "cheap-source-map",
    devServer: {
        open: true,
        host: 'localhost',
        port: 4200,
        historyApiFallback: {
            index: "/index.html",
            verbose: true
        },
        proxy: [
            {
                context: ["/api"],
                target: "http://localhost:8080",
                changeOrigin: true,
                logLevel: 'debug',
                secure: false,
                ws: true
            },
            {
                context: ["/auth"],
                target: "http://localhost:8000",
                changeOrigin: true,
                logLevel: 'debug',
                secure: false,
                ws: true
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: 'index.html',
            scriptLoading: "module"
        }),
        new CopyWebpackPlugin({
            patterns: [
                { from: "images", to: "images" },
                { from: "html", to: "html" },
            ]
        })
    ],
    module: {
        rules: [
            {
                test: /\.(ts)$/i,
                loader: 'ts-loader',
                exclude: ['/node_modules/'],
            },
            {
                test: /\.(eot|svg|ttf|woff|woff2|png|jpg|gif)$/i,
                type: 'asset',
            },
        ],
    },
    resolve: {
        extensions: ['.ts', '.js', '...'],
    }
}

module.exports = env => {
    if (isProduction) {
        config.mode = 'production'
    } else {
        config.mode = 'development'
    }
    return config
}
