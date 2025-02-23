package org.greenstand.android.TreeTracker.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.greenstand.android.TreeTracker.analytics.Analytics
import org.greenstand.android.TreeTracker.database.TreeTrackerDAO
import org.greenstand.android.TreeTracker.database.entity.TreeEntity
import org.greenstand.android.TreeTracker.models.Tree
import org.greenstand.android.TreeTracker.utilities.TimeProvider
import timber.log.Timber

class CreateTreeUseCase(
    private val dao: TreeTrackerDAO,
    private val analytics: Analytics,
    private val timeProvider: TimeProvider,
) : UseCase<Tree, Long>() {

    override suspend fun execute(params: Tree): Long = withContext(Dispatchers.IO) {
        val entity = TreeEntity(
            uuid = params.treeUuid.toString(),
            sessionId = params.sessionId,
            photoPath = params.photoPath,
            photoUrl = null,
            note = params.content,
            longitude = params.meanLongitude,
            latitude = params.meanLatitude,
            createdAt = timeProvider.currentTime(),
            extraAttributes = params.treeCaptureAttributes(),
        )

        Timber.d("Inserting TreeCapture entity $entity")
        analytics.treePlanted()
        dao.insertTree(entity)
    }
}
