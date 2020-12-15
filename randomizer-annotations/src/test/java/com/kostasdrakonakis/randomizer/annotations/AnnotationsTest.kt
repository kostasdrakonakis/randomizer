package com.kostasdrakonakis.randomizer.annotations

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Test
import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

class AnnotationsTest : BaseAnnotationTest() {

    @Test
    fun testRetentionPolicyTargetDocumented() {
        annotations<RandomChar>().forEach {
            val annotation = it.name
            val targetAnnotation = it.annotation<Target>()?.value
            if (targetAnnotation?.isNullOrEmpty() == true) {
                fail("Annotation: $annotation either does not have @Target annotation " +
                    "declared or not specified target value")
            }
            val target = targetAnnotation[0]
            when (it.canonicalName) {
                annotation<RandomChar>(),
                annotation<RandomDouble>(),
                annotation<RandomFloat>(),
                annotation<RandomInt>(),
                annotation<RandomLong>(),
                annotation<RandomShort>(),
                annotation<RandomString>() -> assertThat(target).isEqualTo(ElementType.FIELD)
                else -> fail("Annotation $annotation not present in unit test")
            }
            // For AP to be incremental all annotations need to have CLASS retention policy.
            val retentionPolicy = it.annotation<Retention>()?.value
            assertThat(retentionPolicy).isNotNull()
            assertThat(retentionPolicy).isEqualTo(RetentionPolicy.CLASS)
            val documented = it.annotation<Documented>()
            assertThat(documented).isNotNull()
        }
    }
}