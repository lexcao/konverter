package konverter.processor

import konverter.Konvert
import konverter.service.KonvertProcessService

class KonvertProcessor : BaseProcessor(
    annotation = Konvert::class,
    service = KonvertProcessService()
)
