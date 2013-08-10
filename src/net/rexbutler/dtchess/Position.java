/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess;

import java.util.HashSet;

public class Position extends PositionState {

    public Position(boolean addInitialPieces) {
        super(addInitialPieces);
    }

    public Position(PositionState position) {
        super(position);
    }

    public HashSet<Move> allLegalMoves(boolean strictOnly) {
        HashSet<Move> possibleMoves = new HashSet<>();
        final HashSet<Move> legalMoves = new HashSet<>();

        possibleMoves = possibleMoves();
        for (final Move move : possibleMoves) {
            if (isLegalMove(move, strictOnly)) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    public boolean applyMove(Move move) {
        final boolean isLegalPawnMove = isLegalPawnMove(move); 
        final boolean isLegalKingMove = isLegalKingMove(move);
        final boolean isLegalVectorMove = isLegalVectorMove(move);
        
        if (isLegalKingMove) {
            applyMoveKing(move);
        } else if (isLegalPawnMove) {
            applyMovePawn(move);
        } else if (isLegalVectorMove) {
            applyMoveVector(move);
        } else {
            // The move must be illegal, see construction of
            // MoveLogic.isLegalMove...
            return false;
        }
        
        // Invert the color to move
        colorToMove = colorToMove.invert();

        // Update the status of the castling rights
        updateCastlingRights(move);
        
        // Clear the En Passant square, E.P. capture is available for only one
        // move
        enPassantSquare = null;
        // Reset or increment half move clock
        if (isCapture(move) || isLegalPawnMove) {
            halfMoveClock = 0;
        } else {
            halfMoveClock++;
        }
        fullMoveCount++;

        return true; // TODO
    }

    public void updateCastlingRights(Move move) {
        // If pieces move off of the king or rook castling start square, that castling option is
        // no longer available
        for(CastleLocation castleLocation : CastleLocation.values()) {
            if(move.getStartSquare().equals(castleLocation.getKingMove().getStartSquare())
                    || move.getStartSquare().equals(castleLocation.getRookMove().getStartSquare())) {
                
                castlingRights.put(castleLocation, false);
            }
        }
    }
    
    public boolean applyMoveKing(Move move) {
        if (isLegalKingRegularMove(move)) {
            movePiece(move);
        } else if ((isLegalKingCastlingMove(move))) {
            final CastleLocation castleLocation = CastleLocation.getCastlingType(move);
            final Move rookMove = castleLocation.getRookMove();
            movePiece(move);
            movePiece(rookMove);
        } else {
            assert false : "Unexpected else condition";
        }

        return true;
    }

    public boolean applyMovePawn(Move move) {
        final Piece pawnToMove = board[move.getStartSquare().getX()][move.getStartSquare().getY()];

        final boolean isAdvanceMove = isLegalPawnAdvanceMove(move);
        final boolean isCaptureMove = isLegalPawnCaptureMove(move);
        final boolean isEnPassantMove = isLegalPawnEnPassantMove(move);

        if (isAdvanceMove || isCaptureMove) {
            // A pawn promotion move, of type isLegalPawnAdvanceMove or
            // isLegalPawnCaptureMove
            if (move.getEndSquare().getY() == 0 || move.getEndSquare().getY() == 7) {
                final Piece promotionPiece = new Piece(pawnToMove.getColor(), move.getPromotionPieceType());
                board[move.getEndSquare().getX()][move.getEndSquare().getY()] = promotionPiece;
                board[move.getStartSquare().getX()][move.getStartSquare().getY()] = Piece.NONE;
                // A regular non En Passant Pawn move
            } else {
                movePiece(move);
            }

        } else if (isEnPassantMove) {
            movePiece(move);
            // Clear the En Passant Square
            board[move.getEndSquare().getX()][move.getStartSquare().getY()] = Piece.NONE; // TODO
        } else {
            // Assert Error
            assert false : "Unexpected else condition";
        }
        return true;
    }

    public boolean movePiece(Move move) {
        final Piece pieceToMove = board[move.getStartSquare().getX()][move.getStartSquare().getY()];

        board[move.getEndSquare().getX()][move.getEndSquare().getY()] = pieceToMove;
        board[move.getStartSquare().getX()][move.getStartSquare().getY()] = Piece.NONE; // TODO

        return true; // TODO
    }

    public boolean applyMoveVector(Move move) {
                
        
        movePiece(move);
        return true;
    }

    public HashSet<Move> possibleMoves() {
        final HashSet<Move> moves = new HashSet<>();

        for (int bx = 0; bx < Chess.BOARD_SIZE; bx++) {
            for (int by = 0; by < Chess.BOARD_SIZE; by++) {
                final Square square1 = new Square(bx, by);

                if (!this.getPieceAt(new Square(bx,by)).equals(Piece.NONE)) {
                    moves.addAll(possibleMovesWhichStartAt(square1));
                }
            }
        }
        return moves;
    }

    public HashSet<Move> possibleMovesWhichStartAt(Square square1) {
        final HashSet<Move> moves = new HashSet<>();
        final PieceType pieceType = this.getPieceAt(square1).getType();

        for (final MoveVector moveVector : Chess.pieceVectors.get(pieceType)) {
            final Square square2 = square1.addVector(moveVector);
            if(square2.isOnBoard()) {
                moves.add(new Move(square1, square2));
            }
        }
        return moves;
    }

    public HashSet<Move> possibleMovesWhichEndAt(Square square2) {
        final HashSet<Move> moves = new HashSet<>();
        int x2 = square2.getX();
        int y2 = square2.getY();
        
        for (int x1 = 0; x1 < Chess.BOARD_SIZE; x1++) {
            for (int y1 = 0; y1 < Chess.BOARD_SIZE; y1++) {
                int adx = Math.abs(x2 - x1);
                int ady = Math.abs(y2 - y1);
                
                if(board[x1][y1].equals(Piece.NONE)) {
                    continue;
                }

                if(board[x1][y1].getColor() != getColorToMove()) {
                    continue;
                }
                
                if((adx == 0) && (ady == 0)) {
                    continue;
                }
                
                if( (adx == 0) || (ady == 0) || (adx == ady) || (adx == 2 && ady == 1) || (adx == 1 && ady == 2) ) {
                    Square square1 = new Square(x1,y1);
                    
                    moves.add(new Move(square1,square2));
                    if(board[x1][y1].getType() == PieceType.PAWN) {
                        moves.add(new Move(square1, square2, PieceType.QUEEN));
                        moves.add(new Move(square1, square2, PieceType.KNIGHT));
                        moves.add(new Move(square1, square2, PieceType.ROOK));
                        moves.add(new Move(square1, square2, PieceType.BISHOP));
                    }
                }
            }
        }

        return moves;
    }
    
    public boolean isAttackedByColor(Square target, PieceColor color) {
        Position modPosition;

        if (this.getPieceAt(target).getColor().invert() != color) {
            return false;
        }

        if (this.getColorToMove() != color) {
            modPosition = new Position(this);
            modPosition.setColorToMove(color);
        } else {
            modPosition = this;
        }

        for (int i = 0; i < Chess.BOARD_SIZE; i++) {
            for (int j = 0; j < Chess.BOARD_SIZE; j++) {
                final Square source = new Square(i, j);
                final Move move = new Move(source, target);
                if (modPosition.isLegalMove(move, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isCapture(Move move) {
        final PieceColor pieceColor1 = this.getPieceAt(move.getEndSquare()).getColor();
        final PieceColor pieceColor2 = this.getPieceAt(move.getStartSquare()).getColor();

        if (pieceColor1 == PieceColor.NONE || pieceColor2 == PieceColor.NONE) {
            return false;
        }

        return (pieceColor1 != pieceColor2);
    }

    public boolean isCaptureOfOwnColor(Move move) {
        final PieceColor pieceColor1 = this.getPieceAt(move.getStartSquare()).getColor();
        final PieceColor pieceColor2 = this.getPieceAt(move.getEndSquare()).getColor();

        if (pieceColor1 == PieceColor.NONE) {
            return false;
        }

        return (pieceColor1 == pieceColor2);
    }

    public boolean isKingToMoveInCheck() {
        Position opponentToMovePosition = new Position(this);
        opponentToMovePosition.setColorToMove(this.getColorToMove().invert());
       
        return opponentToMovePosition.isKingLeftInCheck();
    }
    
    public boolean isKingLeftInCheck() {
        HashSet<Move> attackingMoves;
        PieceColor colorToMove = this.getColorToMove();
        int kx = -1; // If search succeeds, these should be set to "real" values
        int ky = -1;
        
        squareloop:
        for(int j = 0; j < Chess.BOARD_SIZE; j++) {
            for(int i = 0; i < Chess.BOARD_SIZE; i++) {
                Piece pieceHere = this.getPieceAt(new Square(i,j));
                if(pieceHere.getType() == PieceType.KING 
                        && pieceHere.getColor() == colorToMove.invert()) {
                    kx = i;
                    ky = j;
                    break squareloop;
                }
            }
        }
        
        assert ((kx != -1) || (ky != -1)) : "Unexpected search failure, inconsistent state.  King not found."; // TODO : Refactor
        Square kingSquare = new Square(kx,ky);
        
        attackingMoves = possibleMovesWhichEndAt(kingSquare);
        
        for(Move move : attackingMoves) {
            if(isLegalVectorMove(move) || isLegalPawnCaptureMove(move)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLegalKingCastlingMove(Move move) {
        HashSet<Square> squaresBetweenKingAndRook;
        HashSet<Square> squaresBetweenKingStartAndEnd;

        if(isKingToMoveInCheck()) {
            return false;
        }
        
        final CastleLocation castleLocation = CastleLocation.getCastlingType(move);
        if (castleLocation == null || !this.getCastleRights(castleLocation)) {
            return false;
        }

        final Square kingStartSquare = move.getStartSquare();
        final Square rookStartSquare = castleLocation.getRookMove().getStartSquare();

        final PieceColor playerToMoveColor = this.getColorToMove();

        // King of appropriate color on castling starting square
        if (this.getPieceAt(kingStartSquare).getType() != PieceType.KING) {
            return false;
        }
        if (this.getPieceAt(rookStartSquare).getColor() != playerToMoveColor) {
            return false;
        }

        // Rook of appropriate color on castling starting square
        if (this.getPieceAt(rookStartSquare).getType() != PieceType.ROOK) {
            return false;
        }
        if (this.getPieceAt(rookStartSquare).getColor() != playerToMoveColor) {
            return false;
        }

        // squares Between King and Rook empty
        squaresBetweenKingAndRook = Chess.getSquaresBetween(castleLocation.getKingMove().getStartSquare(), castleLocation
                .getRookMove().getStartSquare(), false);
        for (final Square betweenSquare : squaresBetweenKingAndRook) {
            if (isAttackedByColor(betweenSquare, playerToMoveColor.invert())) {
                return false;
            }
        }

        // squares (inclusive) Between King Start and End not attacked
        squaresBetweenKingStartAndEnd = Chess.getSquaresBetween(castleLocation.getKingMove().getStartSquare(), castleLocation
                .getKingMove().getEndSquare(), true);
        for (final Square betweenSquare : squaresBetweenKingStartAndEnd) {
            if (isAttackedByColor(betweenSquare, playerToMoveColor.invert())) {
                return false;
            }
        }

        return true;
    }

    public boolean isLegalKingMove(Move move) {
        final boolean regularMove = isLegalKingRegularMove(move);
        final boolean castlingMove = isLegalKingCastlingMove(move);

        return regularMove || castlingMove;
    }

    public boolean isLegalKingRegularMove(Move move) {
        final int x1 = move.getStartSquare().getX();
        final int x2 = move.getEndSquare().getX();

        final int adx = Math.abs(x2 - x1);

        if (adx > 1) {
            return false;
        }

        return isLegalVectorMove(move);
    }

    public boolean isLegalMove(Move move, boolean strictOnly) {
        final PieceType pieceTypeToMove = this.getPieceAt(move.getStartSquare()).getType();
        Position newPosition;

        boolean legal;
        
        if (pieceTypeToMove == PieceType.NONE) {
            return false;
        } else if (pieceTypeToMove == PieceType.KING) {
            legal = isLegalKingMove(move);
        } else if (pieceTypeToMove == PieceType.PAWN) {
            legal = isLegalPawnMove(move);
        } else {
            legal = isLegalVectorMove(move);
        }
        
        // If strictOnly is false, we are done
        // Also, if not legal in the basic sense
        // we know not legal in strict sense
        if (!strictOnly || legal == false) {
            return legal;
        } else {
            // At this point, check if the king is in check 
            newPosition = new Position(this);
            newPosition.applyMove(move);
            
            return !newPosition.isKingLeftInCheck();
        }
    }

    // May be a pawn promotion move
    public boolean isLegalPawnAdvanceMove(Move move) {
        final int x1 = move.getStartSquare().getX();
        final int y1 = move.getStartSquare().getY();
        final int x2 = move.getEndSquare().getX();
        final int y2 = move.getEndSquare().getY();

        final int dx = x2 - x1;
        final int dy = y2 - y1;
        final int ady = Math.abs(dy);

        final Piece pawnToMove = this.getPieceAt(move.getStartSquare());
        final PieceColor pawnColor = pawnToMove.getColor();
        final boolean promotionMove = (move.getPromotionPieceType() != PieceType.NONE);

        if (pawnToMove.getType() != PieceType.PAWN) {
            return false;
        }

        if (!isMovablePieceAtSquare(move.getStartSquare())) {
            return false;
        }
        
        if (isCapture(move) || isCaptureOfOwnColor(move)) {
            return false;
        }

        if (dx != 0) {
            return false;
        }

        if (dy < 0 && pawnColor == PieceColor.WHITE) {
            return false;
        }
        if (dy == 0) {
            return false;
        }
        if (dy > 0 && pawnColor == PieceColor.BLACK) {
            return false;
        }

        if (ady == 0) {
            return false;
        } else if (ady == 1) {
            if (promotionMove) {
                if (pawnColor == PieceColor.WHITE && y2 != Chess.WHITE_PAWN_PROMOTED_Y) {
                    return false;
                } else if (pawnColor == PieceColor.BLACK && y2 != Chess.BLACK_PAWN_PROMOTED_Y) {
                    return false;
                }
            }
        } else if (ady == 2) {
            if (pawnColor == PieceColor.WHITE) {
                if (y1 != Chess.WHITE_PAWN_START_Y || !this.getPieceAt(new Square(x1, y1 + 1)).equals(Piece.NONE)) {
                    return false;
                }
            } else if (pawnColor == PieceColor.BLACK) {
                if (y1 != Chess.BLACK_PAWN_START_Y || !this.getPieceAt(new Square(x1, y1 - 1)).equals(Piece.NONE)) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    // May be a pawn promotion move
    // Normal pawn capture, but not a special case "En Passant" capture
    public boolean isLegalPawnCaptureMove(Move move) {
        final int x1 = move.getStartSquare().getX();
        final int y1 = move.getStartSquare().getY();
        final int x2 = move.getEndSquare().getX();
        final int y2 = move.getEndSquare().getY();

        final int dx = x2 - x1;
        final int dy = y2 - y1;
        final int adx = Math.abs(dx);

        final Piece pawnToMove = this.getPieceAt(move.getStartSquare());
        final PieceColor pawnColor = pawnToMove.getColor();

        if (pawnToMove.getType() != PieceType.PAWN) {
            return false;
        }
        if (!isMovablePieceAtSquare(move.getStartSquare())) {
            return false;
        }

        if (!isCapture(move)) {
            return false;
        }

        if (adx != 1) {
            return false;
        }
        if (pawnColor == PieceColor.WHITE && dy != 1) {
            return false;
        }
        if (pawnColor == PieceColor.BLACK && dy != -1) {
            return false;
        }

        if (pawnColor == PieceColor.WHITE && y2 == Chess.WHITE_PAWN_PROMOTED_Y) {
            if (move.getPromotionPieceType() == PieceType.NONE) {
                return false;
            }
        }

        if (pawnColor == PieceColor.BLACK && y2 == Chess.BLACK_PAWN_PROMOTED_Y) {
            if (move.getPromotionPieceType() == PieceType.NONE) {
                return false;
            }
        }

        return true;
    }

    public boolean isMovablePieceAtSquare(Square startSquare) {
        final Piece pieceToMove = getPieceAt(startSquare);
    
        // There should be a piece on the starting square
        if (pieceToMove.equals(Piece.NONE)) {
            return false;
        }
    
        // The color of the piece being moved must be the color
        // of the player to move
        return (pieceToMove.getColor() == getColorToMove());
    }
    
    // Not a pawn promotion move, of course
    public boolean isLegalPawnEnPassantMove(Move move) {
        final int x1 = move.getStartSquare().getX();
        final int y1 = move.getStartSquare().getY();
        final int x2 = move.getEndSquare().getX();
        final int y2 = move.getEndSquare().getY();

        final int dx = x2 - x1;
        final int dy = y2 - y1;
        final int adx = Math.abs(dx);
        final int ady = Math.abs(dy);

        final Piece pawnToMove = this.getPieceAt(move.getStartSquare());
        final Square enPassantSquare = this.getEnPassantSquare();

        if (!isMovablePieceAtSquare(move.getStartSquare())) {
            return false;
        }
        if (pawnToMove.getType() != PieceType.PAWN) {
            return false;
        }
        if (move.getPromotionPieceType() != PieceType.NONE) {
            return false;
        }

        if (adx != 1 || ady != 1) {
            return false;
        }
        if (dy == Chess.WHITE_PAWN_Y_STEP && pawnToMove.getColor() == PieceColor.BLACK) {
            return false;
        }
        if (dy == Chess.BLACK_PAWN_Y_STEP && pawnToMove.getColor() == PieceColor.WHITE) {
            return false;
        }

        if (!this.getPieceAt(move.getEndSquare()).equals(Piece.NONE)) {
            return false;
        }
        // If an En Passant move is from (x1,y1) to (x2,y2) it
        // captures the piece at (x2,y1)
        if (this.getPieceAt(new Square(x2, y1)).equals(Piece.NONE)) {
            return false;
        }

        if (enPassantSquare == null) {
            return false;
        }
        if (enPassantSquare.getX() != x2 || enPassantSquare.getY() != y2) {
            return false;
        }

        return true;
    }

    public boolean isLegalPawnMove(Move move) {
        final boolean advanceMove = isLegalPawnAdvanceMove(move);
        final boolean captureMove = isLegalPawnCaptureMove(move);
        final boolean enPassantMove = isLegalPawnEnPassantMove(move);

        return advanceMove || captureMove || enPassantMove;
    }

    public boolean isLegalVectorMove(Move move) {
        final Square s1 = move.getStartSquare();
        final Square s2 = move.getEndSquare();

        final PieceType pieceType = this.getPieceAt(move.getStartSquare()).getType();

        if(pieceType == PieceType.PAWN) {
            return false;
        }
        if (!isMovablePieceAtSquare(s1)) {
            return false;
        }
        if (isCaptureOfOwnColor(move)) {
            return false;
        }
        if (move.getPromotionPieceType() != PieceType.NONE) {
            return false;
        }

        if (!Chess.isPossibleVectorForPiece(pieceType, s1, s2)) {
            return false;
        }

        for (final Square square : Chess.getSquaresBetween(s1, s2, false)) {
            if (!this.getPieceAt(square).equals(Piece.NONE)) {
                return false;
            }
        }

        return true;
    }
    
    public boolean isCheckmate() {
        if(allLegalMoves(true).size() == 0) {
            if(isKingToMoveInCheck()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isDraw() {
        if(allLegalMoves(true).size() == 0) {
            if(!isKingToMoveInCheck()) {
                return true;
            }
        }
        return false;
    }
}
