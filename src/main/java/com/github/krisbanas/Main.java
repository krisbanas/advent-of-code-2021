package com.github.krisbanas;

import com.github.krisbanas.solutions.day1.Day3;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Day3().part1()); //1: 4174964 //2: 4474944 Refactor.
    }
}

/**
 * from collections import Counter
 *
 * ll = [x for x in open('input').read().strip().split('\n')]
 *
 * theta = ''
 * epsilon = ''
 * for i in range(len(ll[0])):
 * 	common = Counter([x[i] for x in ll])
 * 	if common['0'] > common['1']:
 * 		theta += '0'
 * 		epsilon += '1'
 * 	else:
 * 		theta += '1'
 * 		epsilon += '0'
 * print(int(theta,2)*int(epsilon,2))
 * Part 2
 *
 * from collections import Counter
 *
 * ll = [x for x in open('input').read().strip().split('\n')]
 *
 * theta = ''
 * epsilon = ''
 * for i in range(len(ll[0])):
 * 	common = Counter([x[i] for x in ll])
 *
 * 	if common['0'] > common['1']:
 * 		ll = [x for x in ll if x[i] == '0']
 * 	else:
 * 		ll = [x for x in ll if x[i] == '1']
 * 	theta = ll[0]
 *
 * ll = [x for x in open('input').read().strip().split('\n')]
 * for i in range(len(ll[0])):
 * 	common = Counter([x[i] for x in ll])
 *
 * 	if common['0'] > common['1']:
 * 		ll = [x for x in ll if x[i] == '1']
 * 	else:
 * 		ll = [x for x in ll if x[i] == '0']
 * 	if ll:
 * 		epsilon = ll[0]
 * print(int(theta,2)*int(epsilon,2))
 */