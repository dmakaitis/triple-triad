package com.portkullis.tripletriad

import com.portkullis.tripletriad.engine.model.Rule
import spock.lang.Specification

import java.util.function.Function

class RuleChangeSolverTest extends Specification {

    def useDrawPoint = new RuleChangeSolver.UseDrawPoint()
    def challengeThenRefuse = new RuleChangeSolver.ChallengeThenRefuse()

    def "Action sequence has the proper outcome"() {
        given:
        def actionFunction = buildActionFunction(local, carried, queen, actions)

        when:
        RuleChangeSolver.SearchNode result = actionFunction.apply(new RuleChangeSolver.SearchNode())

        then:
        result.abolish == abolish
        result.rule == rule

        where:
        local                                                                     | carried                | queen | actions     || abolish | rule
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "dra"       || true    | Rule.ELEMENTAL
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "a"         || false   | Rule.PLUS
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "ra"        || false   | Rule.PLUS
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rra"       || false   | Rule.PLUS
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrra"      || false   | Rule.PLUS
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrra"     || false   | null
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrra"    || false   | null
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrra"   || false   | null
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrrra"  || false   | null
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrrrra" || false   | Rule.PLUS
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrraa"   || false   | Rule.PLUS
        [Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL] | [Rule.OPEN, Rule.PLUS] | false | "rrrrrraa"  || false   | Rule.PLUS
    }

    /////////////////////////////////////////////////////////////

    def buildActionFunction(Collection<Rule> local, Collection<Rule> carried, boolean queen, String actions) {
        Function<RuleChangeSolver.SearchNode, RuleChangeSolver.SearchNode> rVal = null
        def challengeThenAccept = new RuleChangeSolver.ChallengeThenAccept(local, carried, queen)

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
