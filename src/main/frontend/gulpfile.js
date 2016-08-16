require('harmonize')();

var gulp = require('gulp');
var browserify = require('browserify');
var babelify = require('babelify');
var source = require('vinyl-source-stream');
var jest = require('gulp-jest');
var chalk = require('chalk');

gulp.task('compile', function(){
    browserify('./js/productRegister.js')
        .transform(babelify)
        .bundle()
        .on('error', function(err){
            console.log(chalk.bold.red(err));
        })
        .pipe(source('productRegister.js'))
        .pipe(gulp.dest('../webapp/WEB-INF/resources/js'));
});

gulp.task('jest', function(){
    gulp.src('src/**/*-test.js')
        .pipe(jest({
            rootDir : 'src',
            scriptPreprocessor : "../node_modules/babel-jest",
            testFileExtensions : ["es6", "js"],
            moduleFileExtensions : ["js", "json", "es6"],
            unmockedModulePathPatterns : ["./node_modules/react"]
        }));
});

gulp.task('watch', function(){
    gulp.watch(['src/**/*.js*'], ['compile']);
});

gulp.task('default', ['compile']);

gulp.task('test', ['jest']);