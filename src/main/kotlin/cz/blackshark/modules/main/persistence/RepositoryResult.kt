package cz.blackshark.modules.main.persistence

data class RepositoryResult<T>(val items:List<T>, val pageSize:Int, val pages:Int)
