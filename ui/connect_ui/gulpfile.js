"use strict";

var gulp = require('gulp');
var war = require("./index");
var zip = require('gulp-zip');
//var zip = require('../gulp-zip'); // till the pull request is approved

gulp.task('war', function () {
    gulp.src(["connect_ui/**"])
        .pipe(war({
            welcome: 'index.html',
            displayName: 'Connect UI',
        }))
        .pipe(zip('connect_ui.war'))
        .pipe(gulp.dest("./dist"));

});

gulp.task('default', ['war']);