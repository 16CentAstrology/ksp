/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package com.google.devtools.kotlin.symbol.processing.symbol.impl.java

import com.intellij.psi.PsiParameter
import com.google.devtools.kotlin.symbol.processing.symbol.*
import com.google.devtools.kotlin.symbol.processing.symbol.impl.KSObjectCache
import com.google.devtools.kotlin.symbol.processing.symbol.impl.kotlin.KSNameImpl
import com.google.devtools.kotlin.symbol.processing.symbol.impl.toLocation

class KSVariableParameterJavaImpl private constructor(val psi: PsiParameter) : KSVariableParameter {
    companion object : KSObjectCache<PsiParameter, KSVariableParameterJavaImpl>() {
        fun getCached(psi: PsiParameter) = cache.getOrPut(psi) { KSVariableParameterJavaImpl(psi) }
    }

    override val origin = Origin.JAVA

    override val location: Location by lazy {
        psi.toLocation()
    }

    override val annotations: List<KSAnnotation> by lazy {
        psi.annotations.map { KSAnnotationJavaImpl.getCached(it) }
    }

    override val isCrossInline: Boolean = false

    override val isNoInline: Boolean = false

    override val isVararg: Boolean = psi.isVarArgs

    override val isVal: Boolean = false

    override val isVar: Boolean = false

    override val name: KSName? by lazy {
        if (psi.name != null) {
            KSNameImpl.getCached(psi.name!!)
        } else {
            null
        }
    }

    override val type: KSTypeReference by lazy {
        KSTypeReferenceJavaImpl.getCached(psi.type)
    }

    override val hasDefault: Boolean = false

    override fun <D, R> accept(visitor: KSVisitor<D, R>, data: D): R {
        return visitor.visitVariableParameter(this, data)
    }

    override fun toString(): String {
        return name?.asString() ?: "_"
    }
}