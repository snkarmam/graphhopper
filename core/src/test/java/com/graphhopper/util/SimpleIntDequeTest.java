/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * @author Peter Karich
 */
public class SimpleIntDequeTest {
    @Test
    public void testSmall() {
        SimpleIntDeque deque = new SimpleIntDeque(8, 2f);
        assertTrue(deque.isEmpty());
        assertEquals(0, deque.getSize());
    }

    @Test
    public void testEmpty() {
        SimpleIntDeque deque = new SimpleIntDeque(1, 2f);
        deque.push(1);
        assertEquals(1, deque.getSize());
        deque.pop();
        assertEquals(0, deque.getSize());
        deque.push(2);
        assertEquals(1, deque.getSize());
    }

    @Test
    public void testPush() {
        SimpleIntDeque deque = new SimpleIntDeque(8, 2f);

        for (int i = 0; i < 60; i++) {
            deque.push(i);
            assertEquals(i + 1, deque.getSize());
        }

        assertEquals(60, deque.getSize());

        assertEquals(0, deque.pop());
        assertEquals(59, deque.getSize());

        assertEquals(1, deque.pop());
        assertEquals(58, deque.getSize());

        deque.push(2);
        assertEquals(59, deque.getSize());
        deque.push(3);
        assertEquals(60, deque.getSize());

        for (int i = 0; i < 50; i++) {
            assertEquals(i + 2, deque.pop());
        }

        assertEquals(10, deque.getSize());
        assertEquals(39, deque.getCapacity());

        deque.push(123);
        assertEquals(11, deque.getSize());

        assertEquals(52, deque.pop());
        assertEquals(10, deque.getSize());
    }




    /**
     * Nos cas des tests commencent ici (toString()).
     */

    /**
     * Test 1 — toString() retourne une chaîne vide quand la deque est vide.
     * Intention : Vérifier le rendu du cas vide (aucun espace, aucune virgule).
     * Oracle : "" (exactement la chaîne vide).
     * Étapes :
     *  - Arrange : créer une deque vide
     *  - Act     : appeler toString()
     *  - Assert  : comparer avec ""
     */
    @Test
    public void toString_ReturnsEmptyString_WhenDequeIsEmpty() {
        // Intention : vérifier que toString() d’une deque vide est "".
        // Oracle : "" (pas d’espace, pas de virgule).
        // Arrange
        SimpleIntDeque dq = new SimpleIntDeque();

        // Act
        String s = dq.toString();

        // Assert
        assertEquals("", s);
    }

    /**
     * Test 2 — toString() avec un seul élément, sans virgule.
     * Intention : S’assurer qu’un élément unique ne génère aucun séparateur.
     * Oracle : "42" et la chaîne ne contient pas de virgule.
     * Étapes :
     *  - Arrange : push(42)
     *  - Act     : toString()
     *  - Assert  : "42" et absence de virgule
     */
    @Test
    public void toString_ReturnsSingleValue_WithoutComma() {
        // Intention : un seul élément ne doit pas introduire de séparateur.
        // Oracle : "42" et aucune virgule dans la chaîne.
        // Arrange
        SimpleIntDeque dq = new SimpleIntDeque();
        dq.push(42);

        // Act
        String s = dq.toString();

        // Assert
        assertEquals("42", s);
        org.junit.jupiter.api.Assertions.assertFalse(s.contains(","), "Single element must not contain commas");
    }

    /**
     * Test 3 — toString() formate deux éléments avec le séparateur exact ", ".
     * Intention : Vérifier le séparateur et l’absence de séparateurs en tête/fin.
     * Oracle : "10, 20" (exactement virgule + espace).
     * Étapes :
     *  - Arrange : push(10), push(20)
     *  - Act     : toString()
     *  - Assert  : "10, 20" et pas de séparateur en tête/fin
     */
    @Test
    public void toString_FormatsTwoElements_WithSingleCommaSpace() {
        // Intention : confirmer le séparateur exact entre deux éléments.
        // Oracle : "10, 20" (exactement ", ", sans séparateur en tête/fin).
        // Arrange
        SimpleIntDeque dq = new SimpleIntDeque();
        dq.push(10);
        dq.push(20);

        // Act
        String s = dq.toString();

        // Assert
        assertEquals("10, 20", s);
        org.junit.jupiter.api.Assertions.assertFalse(s.endsWith(", ") || s.startsWith(","), "No leading/trailing separators");
    }

    /**
     * Test 4 — toString() reflète le nouveau front après un pop().
     * Intention : Après pop(), le premier élément affiché doit changer.
     * Oracle : pour [7,8,9] puis pop() → "8, 9".
     * Étapes :
     *  - Arrange : push(7), push(8), push(9)
     *  - Act     : pop(), toString()
     *  - Assert  : "8, 9"
     */
    @Test
    public void toString_ReflectsFrontShift_AfterOnePop() {
        // Intention : après pop(), la chaîne doit refléter le nouveau front.
        // Oracle : [7,8,9] puis pop() -> "8, 9".
        // Arrange
        SimpleIntDeque dq = new SimpleIntDeque();
        dq.push(7);
        dq.push(8);
        dq.push(9);

        // Act
        dq.pop(); // remove 7
        String s = dq.toString();

        // Assert
        assertEquals("8, 9", s);
    }

    /**
     * Test 5 — toString() redevient vide après avoir tout dépilé.
     * Intention : Le rendu doit redevenir "" quand la deque est vidée.
     * Oracle : "" après le dernier pop().
     * Étapes :
     *  - Arrange : push(1)
     *  - Act     : pop(), toString()
     *  - Assert  : ""
     */
    @Test
    public void toString_ReturnsEmptyString_AfterPoppingAllElements() {
        // Intention : vider la deque doit donner une chaîne vide.
        // Oracle : "" après le dernier pop().
        // Arrange
        SimpleIntDeque dq = new SimpleIntDeque();
        dq.push(1);

        // Act
        dq.pop(); // now empty
        String s = dq.toString();

        // Assert
        assertEquals("", s);
    }

    /**
     * Test 6 — toString() reste correct après une croissance de capacité.
     * Intention : La croissance interne (Arrays.copyOf) n’affecte pas le format.
     * Oracle : pour initSize=2, push 0..4 → "0, 1, 2, 3, 4".
     * Étapes :
     *  - Arrange : initSize=2, push 0..4 (force growth)
     *  - Act     : toString()
     *  - Assert  : "0, 1, 2, 3, 4"
     */
    @Test
    public void toString_HandlesGrowth_WhenPushingBeyondCapacity() {
        // Intention : la croissance de capacité ne doit pas changer la mise en forme.
        // Oracle : initSize=2, push 0..4 -> "0, 1, 2, 3, 4".
        // Arrange
        SimpleIntDeque dq = new SimpleIntDeque(2); // small to force growth
        dq.push(0);
        dq.push(1);
        dq.push(2); // triggers growth
        dq.push(3);
        dq.push(4);

        // Act
        String s = dq.toString();

        // Assert
        assertEquals("0, 1, 2, 3, 4", s);
    }

    /**
     * Test 7 — toString() reste correct après la compaction du front (branche pop()).
     * Intention : Couvrir la branche qui compacte le tableau quand trop d’éléments ont été dépilés.
     * Oracle : initSize=12, push 0..11, pop 7 → "7, 8, 9, 10, 11".
     * Étapes :
     *  - Arrange : initSize=12, push 0..11
     *  - Act     : pop() × 7, toString()
     *  - Assert  : "7, 8, 9, 10, 11"
     */
    @Test
    public void toString_RemainsCorrect_AfterFrontCompactionOnPop() {
        // Intention : tester la compaction du front exécutée dans pop().
        // Oracle : initSize=12, push 0..11, puis pop 7 fois → "7, 8, 9, 10, 11".
        // Arrange
        SimpleIntDeque dq = new SimpleIntDeque(12); // growFactor defaults to 2
        for (int i = 0; i < 12; i++) dq.push(i);

        // Act
        for (int i = 0; i < 7; i++) dq.pop(); // triggers compaction
        String s = dq.toString();

        // Assert
        assertEquals("7, 8, 9, 10, 11", s);
    }

    /**
     * Test 8 — toString() gère les valeurs extrêmes d’entiers.
     * Intention : Valider que les bornes int sont rendues telles quelles.
     * Oracle : "-2147483648, 0, 2147483647".
     * Étapes :
     *  - Arrange : push(Integer.MIN_VALUE), push(0), push(Integer.MAX_VALUE)
     *  - Act     : toString()
     *  - Assert  : "-2147483648, 0, 2147483647"
     */
    @Test
    public void toString_HandlesExtremeIntegerValues() {
        // Intention : les extrêmes doivent être rendus verbatim sans artefact.
        // Oracle : "-2147483648, 0, 2147483647".
        // Arrange
        SimpleIntDeque dq = new SimpleIntDeque();
        dq.push(Integer.MIN_VALUE);
        dq.push(0);
        dq.push(Integer.MAX_VALUE);

        // Act
        String s = dq.toString();

        // Assert
        assertEquals(Integer.MIN_VALUE + ", 0, " + Integer.MAX_VALUE, s);
    }

    /**
     * Test 9 — toString() correct après vidage complet puis réutilisation.
     * Intention : Après avoir tout dépilé, un nouvel usage doit formater correctement.
     * Oracle : après drain → "", puis push [4,5] → "4, 5".
     * Étapes :
     *  - Arrange : push(1,2,3), pop×3
     *  - Act     : toString() (vide), puis push(4,5), toString()
     *  - Assert  : "" puis "4, 5"
     */
    @Test
    public void toString_CorrectAfterDrainingThenReusing() {
        // Intention : réutilisation propre après un vidage complet.
        // Oracle : après drain -> "", puis push [4,5] -> "4, 5".
        // Arrange
        SimpleIntDeque dq = new SimpleIntDeque();
        dq.push(1);
        dq.push(2);
        dq.push(3);
        dq.pop();
        dq.pop();
        dq.pop(); // now empty

        // Act
        String sEmpty = dq.toString();
        dq.push(4);
        dq.push(5);
        String sAfterReuse = dq.toString();

        // Assert
        assertEquals("", sEmpty);
        assertEquals("4, 5", sAfterReuse);
    }

    /**
     * Test 10 — toString() reste correct avec séquence entremêlée push/pop.
     * Intention : Cas réaliste (mélange d’opérations) — ordre FIFO et séparateurs intacts.
     * Oracle : push[1..5], pop×2, push[6,7,8] → "3, 4, 5, 6, 7, 8".
     * Étapes :
     *  - Arrange : push(1..5)
     *  - Act     : pop×2, push(6,7,8), toString()
     *  - Assert  : "3, 4, 5, 6, 7, 8"
     */
    @Test
    public void toString_ReflectsInterleavedPushPopSequences() {
        // Intention : avec opérations entremêlées, l’ordre et le séparateur doivent rester corrects.
        // Oracle : push [1..5], pop 2 → [3,4,5], puis push [6,7,8] → "3, 4, 5, 6, 7, 8".
        // Arrange
        SimpleIntDeque dq = new SimpleIntDeque(5);
        dq.push(1);
        dq.push(2);
        dq.push(3);
        dq.push(4);
        dq.push(5);

        // Act
        dq.pop(); // remove 1
        dq.pop(); // remove 2
        dq.push(6);
        dq.push(7);
        dq.push(8);
        String s = dq.toString();

        // Assert
        assertEquals("3, 4, 5, 6, 7, 8", s);
    }









    /*

     Test  de java faker
     Intention : valider que toString() formate correctement une séquence d’entiers
     variés (négatifs/positifs/0, doublons possibles) générée via java-faker, et que
     l’ordre FIFO est respecté avant/après quelques pops — le tout SANS utiliser
     java.util.Random dans le code de test.

     Justification du choix de java-faker :
       - Génère rapidement des données hétérogènes pour “stresser” le formatage
         (séparateur exact ", ", absence d’espaces parasites, ordre FIFO).
       - On reste déterministes PAR CONSTRUCTION : on enregistre les valeurs
         générées puis on construit l’oracle à partir de ces mêmes valeurs.
         Aucune graine ni Random n’est nécessaire côté test.
     Données :
       - faker = new com.github.javafaker.Faker(new java.util.Locale("fr"))
       - N = 20 entiers dans [-1000, 1000]
       - pop() 5 fois pour vérifier le rendu après avancement du frontIndex.
     Oracle :
       - Avant pops : "v0, v1, ..., v19"
       - Après pops : "v5, v6, ..., v19"
       - toString() est idempotent (deux appels successifs identiques).
     */
    @Test
    public void toString_withFaker_noRandom_orderAndSeparators() {
        com.github.javafaker.Faker faker = new com.github.javafaker.Faker(new java.util.Locale("fr"));
        SimpleIntDeque dq = new SimpleIntDeque(4, 2f);

        final int N = 20;
        int[] vals = new int[N];

        // Génération via Faker (pas d’usage explicite de java.util.Random dans ce test)
        for (int i = 0; i < N; i++) {
            vals[i] = faker.number().numberBetween(-1000, 1001); // [-1000, 1000]
            dq.push(vals[i]);
        }

        // Oracle avant pops
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < N; i++) {
            if (i > 0) expected.append(", ");
            expected.append(vals[i]);
        }
        assertEquals(expected.toString(), dq.toString(), "Format/séparateurs avant pops");

        // Pop de 5 éléments pour tester le rendu après avancement du frontIndex
        final int POPS = 5;
        for (int i = 0; i < POPS; i++) dq.pop();

        // Oracle après pops
        expected.setLength(0);
        for (int i = POPS; i < N; i++) {
            if (i > POPS) expected.append(", ");
            expected.append(vals[i]);
        }
        assertEquals(expected.toString(), dq.toString(), "Format/séparateurs après pops");

        // Idempotence : deux appels successifs ne doivent rien changer
        String s1 = dq.toString();
        String s2 = dq.toString();
        assertEquals(s1, s2, "toString() doit être idempotent");
    }

}

