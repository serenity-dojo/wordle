package com.serenitydojo.wordle.acceptancetests;

import org.junit.jupiter.api.Tag;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("/features")
@Tag("acceptance")
public class CucumberTestSuite {}
