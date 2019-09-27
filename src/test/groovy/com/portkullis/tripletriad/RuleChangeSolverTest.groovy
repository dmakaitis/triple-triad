package com.portkullis.tripletriad

import com.portkullis.tripletriad.engine.model.Rule
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function

class RuleChangeSolverTest extends Specification {

    def useDrawPoint = new RuleChangeSolver.UseDrawPoint()
    def challengeThenRefuse = new RuleChangeSolver.ChallengeThenRefuse()
    def challengeThenAccept = new RuleChangeSolver.ChallengeThenAccept()

    @Unroll
    def "Action sequence '#actions' has the proper outcome"() {
        given:
        def startNode = new RuleChangeSolver.SearchNode(carried, local, false, false)

        def actionFunction = buildActionFunction(actions)

        when:
        RuleChangeSolver.SearchNode result = actionFunction.apply(startNode)

        then:
        result.terminal == terminal
        result.abolish == abolish
        result.rule == rule

        where:
        local                                                                     | carried                | queen | actions              || terminal | abolish | rule
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "dra"                || true     | true    | Rule.ELEMENTAL
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "a"                  || true     | false   | Rule.PLUS
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "ra"                 || true     | false   | Rule.PLUS
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rra"                || true     | false   | Rule.PLUS
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrra"               || true     | false   | Rule.PLUS
//        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrra"              || false    | false   | null
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrar"             || true     | false   | null
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrraa"             || true     | false   | null
//        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrra"             || false    | false   | null
//        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrara"           || true     | false   | Rule.PLUS
//        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrarra"          || true     | false   | Rule.PLUS
//        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrarrra"         || true     | false   | Rule.PLUS
//        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrra"            || false    | false   | null
//        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrrra"           || false    | false   | null
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrrrra"          || true     | false   | Rule.PLUS
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrrrrr"          || false    | false   | null
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrrrrrr"         || true     | false   | null
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrrrrra"         || true     | false   | null
//        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrarrrrrrrrrrr"  || false    | false   | null
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrarrrrrrrrrrrr" || true     | false   | null
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrarrrrrrrrrrra" || true     | false   | null
//        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrraa"            || true     | false   | Rule.PLUS
//        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrraa"           || true     | false   | Rule.PLUS

    }

    /////////////////////////////////////////////////////////////

    def buildActionFunction(String actions) {
        Function<RuleChangeSolver.SearchNode, RuleChangeSolver.SearchNode> rVal = null

        def actionChars = actions.chars
        for (char c : actionChars) {
            RuleChangeSolver.SearchEdge newEdge
            switch (c) {
                case 'r':
                    newEdge = challengeThenRefuse
                    break
                case 'a':
                    newEdge = challengeThenAccept
                    break
                default:
                    newEdge = useDrawPoint
            }

            if (rVal == null) {
                rVal = newEdge
            } else {
                rVal = rVal.andThen(newEdge)
            }
        }

        return rVal
    }
}
