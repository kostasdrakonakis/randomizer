package com.kostasdrakonakis.randomizer.compiler

import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourcesSubject.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import javax.tools.StandardLocation

@RunWith(JUnit4::class)
class RandomizerProcessorTest {

    @Test
    fun verifyGeneratedFile() {
        assertThat(JavaFileObjects.forResource("test/MyActivity.java"))
            .processedWith(RandomizerProcessor())
            .compilesWithoutError()
            .and()
            .generatesFileNamed(StandardLocation.SOURCE_OUTPUT, "test", "MyActivity_RANDOM_PROPERTY_BINDING.java")
    }
}