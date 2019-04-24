package com.mirf.core.repository

/**
 * Info about [RepositoryCommander]. Used for reports generation
 */
data class RepositoryInfo(val repositoryName: String, val username: String){

        fun copy(): RepositoryInfo {
            return RepositoryInfo(repositoryName, username)
        }
}
