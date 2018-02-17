#lang racket

(require "test.rkt")
(require "cal.rkt")


;; UN PRETTY-PRINTER POUR LE LANGAGE WHILE

;;; TRAITEMENT DES EXPRESSIONS DU LANGAGE WHILE

;;; pretty-prints an expression
;;; pretty-print-expr : expression  -> string U error-report

(define (pretty-print-expr e)
  (cond [(NIL? e) "nil"]
        [(CST? e) (CST->name e)]
        [(VAR? e) (VAR->name e)]
        [(HD? e) (string-append "(hd " (pretty-print-expr (HD->arg e)) ")")]
        [(TL? e) (string-append "(tl " (pretty-print-expr (TL->arg e)) ")")]
        [(CONS? e) (string-append "(cons " (pretty-print-expr (CONS->arg1 e)) " " (pretty-print-expr (CONS->arg2 e)) ")")]
        [(EQ? e) (string-append (pretty-print-expr (CONS->arg1 e)) " =? " (pretty-print-expr (CONS->arg2 e)))]
        ))






(display "\npretty-print-expr")
(test (pretty-print-expr (VAR "X")) "X") 

(test (pretty-print-expr (HD (VAR "X"))) "(hd X)") 

(test (pretty-print-expr (TL (VAR "X"))) "(tl X)") 

(test (pretty-print-expr (CONS (VAR "X") (VAR "Y"))) "(cons X Y)") 

(test (pretty-print-expr (EQ (VAR "X") (VAR "Y"))) "X =? Y") 

(test (pretty-print-expr (EQ (HD (VAR "X")) (CONS (VAR "Y") (VAR "Z")))) 
      "(hd X) =? (cons Y Z)") 


;;; FONCTIONS PRELIMINAIRES AU TRAITEMENT DES COMMANDES ET DES PROGRAMMES DU LANGAGE WHILE


;; default indentation
;; indent-default : int

(define indent-default 1)


(display "\nindent-default")
(test indent-default 1)




;; searchs a list of indentation specifications
;; indents-search : context-name * (list indentation-spec) -> int

(define (indents-search s l)
  (cond[(empty? l) indent-default]
       [(string=? s (fst (car l))) (snd(car l))]
       [else (indents-search s (cdr l))]
       ))


(display "\nindents-search")
(test (indents-search "IF" (list (pair "IF" 2) (pair "DO" 4))) 
      2)

(test (indents-search "DO" (list (pair "IF" 2) (pair "DO" 4))) 
      4)

(test (indents-search "WHILE" (list (pair "IF" 2) (pair "DO" 4))) 
      indent-default)

(test (indents-search "IF" empty) 
      indent-default)



;; makes an indentation
;; make-indent : int -> string

(define (make-indent i)
  (cond[(<= i 0 ) ""]
       [else (string-append " "(make-indent (- i 1)))]
       )
  )


(display "\nmake-indent")
(test (make-indent 0) "")
(test (make-indent 4) "    ")



;;; appends a string before every element of a list of strings
;;; append-string-before-all : string  * (list string) -> (list string)

(define (append-string-before-all s l)
  (cond[(empty? l) empty]
       [else (cons (string-append s (car l)) (append-string-before-all s (cdr l)))]
       )
  )


(display "\nappend-string-before-all")
(test (append-string-before-all "pref" (list "foo" "bar" "zo")) 
      (list "preffoo" "prefbar" "prefzo")) 
(test (append-string-before-all "pref" empty) empty)




;;; appends a string after every element of a list of strings
;;; append-string-after-all : string  *  (list string) -> (list string)

(define (append-string-after-all s l)
  (cond[(empty? l) empty]
       [else (cons (string-append (car l) s) (append-string-after-all s (cdr l)))]
       )
  )


(display "\nappend-string-after-all")
(test (append-string-after-all "suff" (list "foo" "bar" "zo")) 
      (list "foosuff" "barsuff" "zosuff")) 
(test (append-string-after-all "pref" empty) empty)




;;; appends a string after every element of a list of strings except the last one
;;; append-string-after-all-but-last : string  *  (list string) -> (list string)

(define (append-string-after-all-but-last s l)
  (cond[(empty? l) empty]
       [(= (length l) 1) l]
       [else (cons (string-append (car l) s) (append-string-after-all-but-last s (cdr l)))]
       )
  )


(display "\nappend-string-after-all-but-last")
(test (append-string-after-all-but-last "suff" (list "foo" "bar" "zo")) 
      (list "foosuff" "barsuff" "zo")) 
(test (append-string-after-all-but-last "suff" (list "foo")) 
      (list "foo"))
      (test (append-string-after-all-but-last "pref" empty) empty)



;;; TRAITEMENT DES COMMANDES DU LANGAGE WHILE


;;; pretty-prints a command
;;; pretty-print-command : command * (list indentation-spec) -> (list string) U error-report


(define (pretty-print-command c l)
  (cond[(NOP? c) (cons "nop" empty)]
       [(SET? c) (cons (string-append (pretty-print-expr(SET->var c)) " := " (pretty-print-expr(SET->expr c))) empty)]
       [(WHILE? c) (append (cons (string-append "while " (pretty-print-expr(WHILE->cond c)) " do") empty)
                           (append-string-before-all (make-indent (indents-search "WHILE" l)) (pretty-print-commands (WHILE->body c) l))
                           (cons "od" empty))]
       [(FOR? c) (append (cons (string-append "for " (pretty-print-expr(FOR->count c)) " do") empty)
                           (append-string-before-all (make-indent (indents-search "FOR" l)) (pretty-print-commands (FOR->body c) l))
                           (cons "od" empty))]
       [(IF? c) (append (cons (string-append "if " (pretty-print-expr(IF->cond c))) empty)
                           (cons "then" (append-string-before-all (make-indent (indents-search "IF" l)) (pretty-print-commands (IF->then c) l)))
                           (cons "else" (append-string-before-all (make-indent (indents-search "IF" l)) (pretty-print-commands (IF->else c) l)))
                           (cons "fi" empty))]
    ))
                                 


(display "\npretty-print-command")
(test (pretty-print-command NOP empty) (list "nop"))
(test (pretty-print-command (SET (VAR "X") (CONS (VAR "X") (VAR "Y"))) empty) 
      (list "X := (cons X Y)"))




;;; pretty-prints a list of commands
;;; pretty-print-commands : (list command) * (list indentation-spec) -> (list string) U error-report

(define (pretty-print-commands lc li)
  (list-string (point-virgule (pretty-commands-rec lc li)))
  )

;;; met un point virgule après les commandes d'une ligne 
(define (point-virgule lc)
  (cond [(empty? lc) empty]
        [(= (length lc) 1) lc]
        [(= (length (car lc)) 1) (cons (list (string-append (car (car lc)) " ;")) (point-virgule (cdr lc)))]
        [else (cons (point-virgule-list (car lc)) (point-virgule (cdr lc)))]
        )
  )

;;; met un point virgule au dernier element d'une list

(define (point-virgule-list lc)
  (cond [(empty? lc) empty]
        [(= (length lc) 1) (list (string-append (car lc) " ;"))]
        [else (cons (car lc) (point-virgule-list (cdr lc)))]
        )
  )

;;; fait la recursion des commands
(define (pretty-commands-rec lc li)
  (cond[(empty? lc) empty]
       [(= (length lc) 1) (list (pretty-print-command (car lc) li))]
       [else (cons (pretty-print-command (car lc) li) (pretty-commands-rec (cdr lc) li))]
       )
  )

;;; reconstruit une liste de string
(define (list-string li)
  (cond [(empty? li) empty]
        [(= (length li) 1) (car li)]
        [else (append (car li) (list-string (cdr li)))]
        )
  )


(display "\npretty-print-commands")
(test (pretty-print-commands (list (SET (VAR "X") (CONS (VAR "X") (VAR "Y")))) empty) 
      (list "X := (cons X Y)"))

(test (pretty-print-commands (list (SET (VAR "X") (CONS (VAR "X") (VAR "Y"))) 
                                   (SET (VAR "A") (CONS (VAR "B") (VAR "C")))) empty) 
      (list "X := (cons X Y) ;" "A := (cons B C)"))


(display "\npretty-print-command pour while")
(test (pretty-print-command (WHILE (EQ (VAR "X") (VAR "Y")) 
                                   (list (SET (VAR "X") 
                                              (CONS (VAR "X") 
                                                    (VAR "Y"))))) 
                            empty) 
      (list "while X =? Y do" " X := (cons X Y)" "od"))

(test (pretty-print-command (WHILE (EQ (VAR "X") (VAR "Y")) 
                                   (list (SET (VAR "X") (CONS (VAR "X") (VAR "Y"))) 
                                         (SET (VAR "A") (CONS (VAR "B") (VAR "C"))))) 
                            empty) 

      (list "while X =? Y do" " X := (cons X Y) ;" " A := (cons B C)" "od"))

(test (pretty-print-command (WHILE (EQ (VAR "X") (VAR "Y")) (list (SET (VAR "X") (CONS (VAR "X") (VAR "Y"))))) (list (pair "WHILE" 5))) 
      (list "while X =? Y do" "     X := (cons X Y)" "od"))

(test (pretty-print-command (WHILE (EQ (VAR "X") (VAR "Y")) (list (SET (VAR "X") (CONS (VAR "X") (VAR "Y"))) (SET (VAR "A") (CONS (VAR "B") (VAR "C"))))) (list (pair "WHILE" 5))) 
      (list "while X =? Y do" "     X := (cons X Y) ;" "     A := (cons B C)" "od"))


(display "\npretty-print-command pour for")
(test (pretty-print-command (FOR (VAR "X") 
                                 (list (SET (VAR "X") 
                                            (CONS (VAR "X") 
                                                  (VAR "Y"))))) 
                            empty) 
      (list "for X do" " X := (cons X Y)" "od"))

(test (pretty-print-command (FOR (VAR "X")  
                                 (list (SET (VAR "X") (CONS (VAR "X") (VAR "Y"))) 
                                       (SET (VAR "A") (CONS (VAR "B") (VAR "C"))))) 
                            empty) 

      (list "for X do" " X := (cons X Y) ;" " A := (cons B C)" "od"))

(test (pretty-print-command (FOR (VAR "X") (list (SET (VAR "X") (CONS (VAR "X") (VAR "Y"))))) (list (pair "FOR" 5))) 
      (list "for X do" "     X := (cons X Y)" "od"))

(test (pretty-print-command (FOR (VAR "X") (list (SET (VAR "X") (CONS (VAR "X") (VAR "Y"))) (SET (VAR "A") (CONS (VAR "B") (VAR "C"))))) (list (pair "FOR" 5))) 
      (list "for X do" "     X := (cons X Y) ;" "     A := (cons B C)" "od"))


(display "\npretty-print-command pour if")
(test (pretty-print-command (IF (EQ (VAR "X")(CONS NIL NIL))
                                (list (SET (VAR "X") (CONS NIL (VAR "X"))))
                                (list (SET (VAR "X") NIL )))
            (list (pair "IF" 4)))
      (list "if X =? (cons nil nil)" "then" "    X := (cons nil X)" "else" "    X := nil" "fi"))

(test (pretty-print-command (IF (EQ (VAR "X")(CONS NIL NIL))
                                (list (SET (VAR "X") (CONS NIL (VAR "X"))))
                                (list (SET (VAR "X") NIL )))
            empty)
      (list "if X =? (cons nil nil)" "then" " X := (cons nil X)" "else" " X := nil" "fi"))


;;; pretty prints an input list
;;; pretty-print-in : (list variable) * (list indentation-spec) -> string U error-report

(define (pretty-print-in lc li)
  (cond [(empty? lc) ""]
        [(= (length lc) 1) (pretty-print-expr (car lc))]
        [else (string-append (pretty-print-expr (car lc)) ", " (pretty-print-in (cdr lc) li))]
        )
  )


(display "\npretty-print-in")
(test (pretty-print-in (list (VAR "X")) empty) "X")
(test (pretty-print-in (list (VAR "X") (VAR "Y")) empty) "X, Y")




;;; pretty-prints an output list
;;; pretty-print-out : (list variable) * (list indentation-spec) -> string U error-report

(define (pretty-print-out lc li)
  (cond [(empty? lc) ""]
        [(= (length lc) 1) (pretty-print-expr (car lc))]
        [else (string-append (pretty-print-expr (car lc)) ", " (pretty-print-in (cdr lc) li))]
        )
  )


(display "\npretty-print-out")
(test (pretty-print-out (list (VAR "X")) empty) "X")
(test (pretty-print-out (list (VAR "X") (VAR "Y")) empty) "X, Y")




;;; pretty-prints a while program
;;; pretty-print-progr : program * indentation-specs -> (list string) U error-report

(define (pretty-print-progr p li)
  (cond [(PROGR? p) (append (cons (string-append "read " (pretty-print-in (PROGR->in p) li)) empty) (cons "%" empty) (append-string-before-all (make-indent (indents-search "" li))(pretty-print-commands (PROGR->body p) li)) (cons "%" empty) (cons (string-append "write " (pretty-print-out (PROGR->out p) li)) empty))
         ]
        [else empty]
)
  )


(display "\npretty-print-progr")
(test (pretty-print-progr (PROGR (list (VAR "X")) 
                                 (list (SET (VAR "Y") NIL) 
                                       (WHILE (VAR "X") 
                                              (list (SET (VAR "Y") (CONS (HD (VAR "X")) (VAR "Y"))) 
                                                    (SET (VAR "X") (TL (VAR "X"))))) )
                                 (list (VAR "Y"))) empty) 
      (list "read X" "%" " Y := nil ;" " while X do" "  Y := (cons (hd X) Y) ;" 
            "  X := (tl X)" " od" "%" "write Y"))


(test (pretty-print-progr (PROGR (list (VAR "X")) 
                                 (list (SET (VAR "Y") NIL) 
                                       (WHILE (VAR "X") 
                                              (list (SET (VAR "Y") (CONS (HD (VAR "X")) (VAR "Y"))) 
                                                    (SET (VAR "X") (TL (VAR "X"))))) )
                                 (list (VAR "Y"))) (list (pair "WHILE" 5))) 
      (list "read X" "%" " Y := nil ;" " while X do" 
            "      Y := (cons (hd X) Y) ;" "      X := (tl X)" " od" "%" "write Y"))
(test (pretty-print-progr (PROGR (list (VAR "X")) 
                                 (list (SET (VAR "Y") NIL) 
                                       (WHILE (VAR "X") 
                                              (list (SET (VAR "Y") (CONS (HD (VAR "X")) (VAR "Y")))
                                                    (WHILE (VAR "X") 
                                                           (list (SET (VAR "Y") (CONS (HD (VAR "X")) (VAR "Y")))
                                                                 (SET (VAR "X") (TL (VAR "X")))))  
                                                    (SET (VAR "X") (TL (VAR "X"))))) )
                                 (list (VAR "Y"))) (list (pair "WHILE" 5))) 
      (list "read X" "%" " Y := nil ;" " while X do" 
            "      Y := (cons (hd X) Y) ;" "      while X do" 
            "           Y := (cons (hd X) Y) ;" "           X := (tl X)" 
            "      od ;" "      X := (tl X)" " od" "%" "write Y"))




;;; pretty-prints a while program
;;; pretty-print : program x [(list indentation-spec)] -> string U error-report





;(display "pretty-print")
;(test (pretty-print (PROGR (list (VAR "X")) 
;                           (list (SET (VAR "Y") NIL) 
;                                 (WHILE (VAR "X") 
;                                        (list (SET (VAR "Y") (CONS (HD (VAR "X")) (VAR "Y"))) 
;                                              (SET (VAR "X") (TL (VAR "X"))))) )
;                           (list (VAR "Y")))) 
;      "read X
;%
; Y := nil ;
; while X do
;  Y := (cons (hd X) Y) ;
;  X := (tl X)
; od
;%
;write Y")
;(newline)
;(display (pretty-print (PROGR (list (VAR "X")) 
;                           (list (SET (VAR "Y") NIL) 
;                                 (WHILE (VAR "X") 
;                                        (list (SET (VAR "Y") (CONS (HD (VAR "X")) (VAR "Y"))) 
;                                              (SET (VAR "X") (TL (VAR "X"))))) )
;                           (list (VAR "Y")))))


;(test (pretty-print (PROGR (list (VAR "X")) 
;                           (list (SET (VAR "Y") NIL) 
;                                 (WHILE (VAR "X") 
;                                        (list (SET (VAR "Y") (CONS (HD (VAR "X")) (VAR "Y")))
;                                              (WHILE (VAR "X") 
;                                                     (list (SET (VAR "Y") 
;                                                                (CONS (HD (VAR "X")) (VAR "Y")))               
;                                                           (SET (VAR "X") (TL (VAR "X")))))  
;                                              (SET (VAR "X") (TL (VAR "X"))))) )
;                           (list (VAR "Y"))) 
;                    (pair "WHILE" 5) (pair "PROGR" 2)) 
;      "read X
;%
;  Y := nil ;
;  while X do
;       Y := (cons (hd X) Y) ;
;       while X do
;            Y := (cons (hd X) Y) ;
;            X := (tl X)
;       od ;
;       X := (tl X)
;  od
;%
;write Y")
;(newline)
;(display (pretty-print (PROGR (list (VAR "X")) 
;                           (list (SET (VAR "Y") NIL) 
;                                 (WHILE (VAR "X") 
;                                        (list (SET (VAR "Y") (CONS (HD (VAR "X")) (VAR "Y")))
;                                              (WHILE (VAR "X") 
;                                                     (list (SET (VAR "Y") 
;                                                                (CONS (HD (VAR "X")) (VAR "Y")))               
;                                                           (SET (VAR "X") (TL (VAR "X")))))  
;                                              (SET (VAR "X") (TL (VAR "X"))))) )
;                           (list (VAR "Y"))) 
;                    (pair "WHILE" 5) (pair "PROGR" 2)))
;(newline)
