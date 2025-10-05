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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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





/*
     Nos 10 tests commencent ici
     */


    /*
     Intention : vérifier que toString() d’une file vide renvoie une chaîne vide (aucun espace/virgule).
     Données : deque fraîchement créée (capacité 4, grow=2).
     Oracle : "" (chaîne de longueur 0).
     */
    @Test
    public void toString_empty_returnsEmptyString() {
        SimpleIntDeque dq = new SimpleIntDeque(4, 2f);
        assertEquals("", dq.toString());
    }

    /*
     Intention : un seul élément négatif doit s’afficher sans virgule ni espace.
     Données : push(-5).
     Oracle : "-5".
     */
    @Test
    public void toString_singleNegative_noCommaOrSpace() {
        SimpleIntDeque dq = new SimpleIntDeque(4, 2f);
        dq.push(-5);
        assertEquals("-5", dq.toString());
    }

    /*
     Intention : deux éléments doivent être séparés exactement par ", " (virgule+espace).
     Données : push(1), push(2).
     Oracle : "1, 2".
     */
    @Test
    public void toString_twoElements_commaSpaceSeparator() {
        SimpleIntDeque dq = new SimpleIntDeque(4, 2f);
        dq.push(1);
        dq.push(2);
        assertEquals("1, 2", dq.toString());
    }

    /*
     Intention : plusieurs éléments doivent être listés dans l’ordre FIFO avec séparateur correct.
     Données : push(0..4).
     Oracle : "0, 1, 2, 3, 4".
     */
    @Test
    public void toString_multipleElements_orderAndSeparators() {
        SimpleIntDeque dq = new SimpleIntDeque(4, 2f);
        for (int i = 0; i <= 4; i++) dq.push(i);
        assertEquals("0, 1, 2, 3, 4", dq.toString());
    }

    /*
     Intention : après des pops (frontIndex > 0), il ne doit pas y avoir de virgule/espaces en tête.
     Données : push(0..4), pop() x3 -> restent [3,4].
     Oracle : "3, 4".
     */
    @Test
    public void toString_afterPops_noLeadingCommaOrSpace() {
        SimpleIntDeque dq = new SimpleIntDeque(8, 2f);
        for (int i = 0; i <= 4; i++) dq.push(i);
        dq.pop(); dq.pop(); dq.pop();
        assertEquals("3, 4", dq.toString());
    }

    /*
     Intention : après déclenchement du compactage, le rendu doit rester correct.
     Données : init(10,2), push(0..9), pop() x6 -> compactage; restent [6,7,8,9].
     Oracle : "6, 7, 8, 9".
     */
    @Test
    public void toString_afterCompaction_correctContentAndFormat() {
        SimpleIntDeque dq = new SimpleIntDeque(10, 2f);
        for (int i = 0; i < 10; i++) dq.push(i);
        for (int i = 0; i < 6; i++) dq.pop(); // frontIndex dépasse floor(10/2)=5 => compactage
        assertEquals("6, 7, 8, 9", dq.toString());
    }

    /*
     Intention : après croissance (copyOf) due au dépassement de capacité, l’ordre et la mise en forme restent corrects.
     Données : init(2,2), push(0..4) => croissance 2->4->8.
     Oracle : "0, 1, 2, 3, 4".
     */
    @Test
    public void toString_acrossGrowthBoundary_preservesOrderAndFormat() {
        SimpleIntDeque dq = new SimpleIntDeque(2, 2f);
        for (int i = 0; i <= 4; i++) dq.push(i);
        assertEquals("0, 1, 2, 3, 4", dq.toString());
    }

    /*
     Intention : valeurs extrêmes doivent être rendues telles quelles et dans l’ordre.
     Données : push(Integer.MIN_VALUE), push(0), push(Integer.MAX_VALUE).
     Oracle : "<MIN>, 0, <MAX>".
     */
    @Test
    public void toString_extremeInts_renderExactly() {
        SimpleIntDeque dq = new SimpleIntDeque(3, 2f);
        dq.push(Integer.MIN_VALUE);
        dq.push(0);
        dq.push(Integer.MAX_VALUE);
        assertEquals(Integer.MIN_VALUE + ", 0, " + Integer.MAX_VALUE, dq.toString());
    }

    /*
     Intention : idempotence et absence d’effet de bord — appeler toString() n’altère pas l’état.
     Données : push(1,2,3), appeler toString() deux fois puis pop().
     Oracle : deux rendus identiques "1, 2, 3" et pop() renvoie 1 (preuve que toString n’a rien modifié).
     */
    @Test
    public void toString_isIdempotent_andDoesNotMutateState() {
        SimpleIntDeque dq = new SimpleIntDeque(4, 2f);
        dq.push(1); dq.push(2); dq.push(3);
        String s1 = dq.toString();
        String s2 = dq.toString();
        assertEquals("1, 2, 3", s1);
        assertEquals(s1, s2);
        assertEquals(1, dq.pop(), "toString() ne doit pas muter l’ordre/état interne");
    }

    /*
     Intention : doublons et négatifs — vérifier rendu correct y compris après pops partiels.
     Données : push(-1,-1,0,2,2,-3), puis pop() x2 -> restent [0,2,2,-3].
     Oracle : avant pops "-1, -1, 0, 2, 2, -3", après pops "0, 2, 2, -3".
     */
    @Test
    public void toString_duplicatesAndNegatives_beforeAndAfterPops() {
        SimpleIntDeque dq = new SimpleIntDeque(8, 2f);
        int[] vals = {-1, -1, 0, 2, 2, -3};
        for (int v : vals) dq.push(v);
        assertEquals("-1, -1, 0, 2, 2, -3", dq.toString());
        dq.pop(); dq.pop();
        assertEquals("0, 2, 2, -3", dq.toString());
    }
}
