package konverter.processor

import konverter.Konvertable
import konverter.service.KonvertableProcessService

class KonvertableProcessor : BaseProcessor(
    annotation = Konvertable::class,
    service = KonvertableProcessService()
)