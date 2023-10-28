package bingo.service.exception

class EmptyColumnException(message: String) : IllegalStateException(message)

class TicketContentException(message: String) : IllegalStateException(message)

class StripeLengthException(message: String) : IllegalStateException(message)

class StripeContentException(message: String) : IllegalStateException(message)
