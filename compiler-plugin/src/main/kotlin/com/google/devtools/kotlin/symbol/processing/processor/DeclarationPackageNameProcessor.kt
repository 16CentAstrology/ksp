/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package com.google.devtools.kotlin.symbol.processing.processor

import com.google.devtools.kotlin.symbol.processing.processing.Resolver
import com.google.devtools.kotlin.symbol.processing.symbol.KSClassDeclaration
import com.google.devtools.kotlin.symbol.processing.symbol.KSFunctionDeclaration
import com.google.devtools.kotlin.symbol.processing.symbol.KSNode
import com.google.devtools.kotlin.symbol.processing.symbol.KSPropertyDeclaration
import com.google.devtools.kotlin.symbol.processing.visitor.KSTopDownVisitor

class DeclarationPackageNameProcessor : AbstractTestProcessor() {
    val result = mutableListOf<String>()

    override fun toResult(): List<String> {
        return result
    }

    override fun process(resolver: Resolver) {
        val visitor = NameCollector()
        resolver.getAllFiles().map { it.accept(visitor, result) }
    }
}

class NameCollector : KSTopDownVisitor<MutableCollection<String>, Unit>() {

    override fun defaultHandler(node: KSNode, data: MutableCollection<String>) {}

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: MutableCollection<String>) {
        classDeclaration.packageName.asString().let { data.add("${if (it == "") "<no name>" else it}:${classDeclaration.simpleName.asString()}") }
        super.visitClassDeclaration(classDeclaration, data)
    }

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: MutableCollection<String>) {
        function.packageName.asString().let { data.add("${if (it == "") "<no name>" else it}:${function.simpleName.asString()}") }
        super.visitFunctionDeclaration(function, data)
    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: MutableCollection<String>) {
        property.packageName.asString().let { data.add("${if (it == "") "<no name>" else it}:${property.simpleName.asString()}") }
        super.visitPropertyDeclaration(property, data)
    }
}