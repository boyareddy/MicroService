"use strict";

var gulp = require('gulp');
var war = require("./index");
var zip = require('gulp-zip');

gulp.task('war', function () {
    gulp.src(["admin_ui/**"])
        .pipe(war({
            welcome: 'index.html',
            displayName: 'Admin UI',
        }))
        .pipe(zip('admin_ui.war'))
        .pipe(gulp.dest("./dist"));

});

gulp.task('default', ['war']);