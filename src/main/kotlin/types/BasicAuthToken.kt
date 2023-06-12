package types

import auth.TokenFactory.BASIC_AUTH_HEADER_TOKEN

@JvmInline
value class BasicAuthToken(val value: String? = BASIC_AUTH_HEADER_TOKEN)