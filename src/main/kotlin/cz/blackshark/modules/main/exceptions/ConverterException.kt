package cz.blackshark.modules.main.exceptions

import kotlin.reflect.KClass

class ConverterException(val sourceType: KClass<*>, val dstType: KClass<*>, msg: String) : Exception("Can not convert ${sourceType.simpleName} to ${dstType.simpleName}. $msg") {
}
