const HtmlWebpackPlugin = require('html-webpack-plugin');
const ErrorOverlayPlugin = require('error-overlay-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const ESLintPlugin = require('eslint-webpack-plugin');
const { WebpackManifestPlugin } = require('webpack-manifest-plugin');
const { BundleAnalyzerPlugin } = require('webpack-bundle-analyzer');
const CompressionPlugin = require('compression-webpack-plugin');

const path = require('path');

module.exports = (env, options) => {
  return {
    entry: './index.js',

    output: {
      path: path.join(__dirname, '/dist'),
      filename: '[name].js',
      publicPath: '/',
      clean: true,
    },

    module: {
      rules: [
        {
          test: /\.(js|jsx)$/,
          exclude: /node_modules/,
          use: ['babel-loader'],
        },
        {
          test: /\.(css|scss|sass)$/,
          use: [
            options.mode === 'development'
              ? 'style-loader'
              : MiniCssExtractPlugin.loader,
            'css-loader',
            'sass-loader',
          ],
        },
        {
          test: /\.(woff2|woff|ttf)$/i,
          type: 'asset/resource',
          generator: {
            filename: 'fonts/[name][ext]',
          },
        },
      ],
    },

    resolve: {
      extensions: ['.js', '.jsx', '.scss'],
    },

    devServer: {
      contentBase: path.join(__dirname, '/dist'),
      publicPath: '/',
      hot: true,
      inline: true,
      host: 'localhost',
      port: 3000,
      historyApiFallback: true,
    },

    devtool: options.mode === 'production' ? false : 'inline-source-map',

    plugins: [
      new ErrorOverlayPlugin(),
      new ESLintPlugin(),
      new HtmlWebpackPlugin({
        template: 'index.html',
        favicon: './public/favicon.ico',
      }),
      new WebpackManifestPlugin({
        fileName: 'manifest.json',
        basePath: './public/',
      }),
      new BundleAnalyzerPlugin(),
      new MiniCssExtractPlugin({
        filename: `[name].[chunkhash].css`,
      }),
      new CompressionPlugin({
        algorithm: 'gzip',
        test: /\.(js|css|html|ttf)$/,
      }),
    ],

    optimization: {
      minimize: options.mode === 'development' ? false : true,
      splitChunks: {
        cacheGroups: {
          default: false,
          vendors: false,
          defaultVendors: false,
          framework: {
            chunks: 'all',
            name: 'framework',
            filename: '[name].js',
            test: /(?<!node_modules.*)[\\/]node_modules[\\/](react|react-dom|react-router-dom)[\\/]/,
            priority: 30,
          },
          library: {
            chunks: 'all',
            minChunks: 2,
            priority: 20,
            test: /[\\/]node_modules[\\/]/,
            name: 'library',
            filename: '[name].js',
            reuseExistingChunk: true,
          },
          repetition: {
            chunks: 'all',
            minChunks: 2,
            priority: 10,
            test: /[\\/]src[\\/]/,
            name: 'repetition',
            filename: '[name].js',
            reuseExistingChunk: true,
          },
        },
      },
    },

    target: 'web',

    performance: {
      hints: false,
    },
  };
};
