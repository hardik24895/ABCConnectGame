package com.example.abclearngame.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <P, R> CoroutineScope.executeAsyncTask(
    doInBackground: suspend (suspend (P) -> Unit) -> R,
    onPostExecute: (R) -> Unit,
    onProgressUpdate: (P) -> Unit
) = launch {
    val result = withContext(Dispatchers.IO) {
        doInBackground {
            withContext(Dispatchers.Main) { onProgressUpdate(it) }
        }
    }
    onPostExecute(result)
}