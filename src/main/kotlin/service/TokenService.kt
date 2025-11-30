package service

import models.dto.TokenClaim
import models.dto.TokenConfig

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ) : String
}