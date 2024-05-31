package week2.kim.week2_1.heap;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * [디스크 컨트롤러]
 *
 * 하드디스크는 한 번에 하나의 작업만 수행할 수 있습니다. 디스크 컨트롤러를 구현하는 방법은 여러 가지가 있습니다. 가장 일반적인 방법은 요청이 들어온 순서대로 처리하는 것입니다.
 *
 * 예를들어
 *
 * - 0ms 시점에 3ms가 소요되는 A작업 요청
 * - 1ms 시점에 9ms가 소요되는 B작업 요청
 * - 2ms 시점에 6ms가 소요되는 C작업 요청
 *
 * 와 같은 요청이 들어왔습니다. 이를 그림으로 표현하면 아래와 같습니다.
 *
 * Screen Shot 2018-09-13 at 6.34.58 PM.png
 *
 * 한 번에 하나의 요청만을 수행할 수 있기 때문에 각각의 작업을 요청받은 순서대로 처리하면 다음과 같이 처리 됩니다.
 *
 * Screen Shot 2018-09-13 at 6.38.52 PM.png
 *
 * - A: 3ms 시점에 작업 완료 (요청에서 종료까지 : 3ms)
 * - B: 1ms부터 대기하다가, 3ms 시점에 작업을 시작해서 12ms 시점에 작업 완료(요청에서 종료까지 : 11ms)
 * - C: 2ms부터 대기하다가, 12ms 시점에 작업을 시작해서 18ms 시점에 작업 완료(요청에서 종료까지 : 16ms)
 *
 * 이 때 각 작업의 요청부터 종료까지 걸린 시간의 평균은 10ms(= (3 + 11 + 16) / 3)가 됩니다.
 *
 * 하지만 A → C → B 순서대로 처리하면
 *
 * Screen Shot 2018-09-13 at 6.41.42 PM.png
 *
 * - A: 3ms 시점에 작업 완료(요청에서 종료까지 : 3ms)
 * - C: 2ms부터 대기하다가, 3ms 시점에 작업을 시작해서 9ms 시점에 작업 완료(요청에서 종료까지 : 7ms)
 * - B: 1ms부터 대기하다가, 9ms 시점에 작업을 시작해서 18ms 시점에 작업 완료(요청에서 종료까지 : 17ms)
 *
 * 이렇게 A → C → B의 순서로 처리하면 각 작업의 요청부터 종료까지 걸린 시간의 평균은 9ms(= (3 + 7 + 17) / 3)가 됩니다.
 *
 * 각 작업에 대해 [작업이 요청되는 시점, 작업의 소요시간]을 담은 2차원 배열 jobs가 매개변수로 주어질 때, 작업의 요청부터 종료까지 걸린 시간의 평균을 가장 줄이는 방법으로 처리하면 평균이 얼마가 되는지 return 하도록 solution 함수를 작성해주세요. (단, 소수점 이하의 수는 버립니다)
 *
 * 제한 사항
 * jobs의 길이는 1 이상 500 이하입니다.
 * jobs의 각 행은 하나의 작업에 대한 [작업이 요청되는 시점, 작업의 소요시간] 입니다.
 * 각 작업에 대해 작업이 요청되는 시간은 0 이상 1,000 이하입니다.
 * 각 작업에 대해 작업의 소요시간은 1 이상 1,000 이하입니다.
 * 하드디스크가 작업을 수행하고 있지 않을 때에는 먼저 요청이 들어온 작업부터 처리합니다.
 *
 * 입출력 예
 * jobs	                        return
 * [[0, 3], [1, 9], [2, 6]]	    9
 *
 * 입출력 예 설명
 * 문제에 주어진 예와 같습니다.
 *
 * 0ms 시점에 3ms 걸리는 작업 요청이 들어옵니다.
 * 1ms 시점에 9ms 걸리는 작업 요청이 들어옵니다.
 * 2ms 시점에 6ms 걸리는 작업 요청이 들어옵니다.
 *
 *
 *
 *
 *
 * 작업을 요청 시간 순으로 정렬
 * 현재 시간 이전에 요청된 모든 작업을 우선순위 큐에 추가 (이 큐는 작업의 소요 시간을 기준으로 정렬)
 * 큐가 비어 있지 않으면 가장 짧은 작업을 처리
 *  그렇지 않으면 다음 작업의 요청 시간으로 현재 시간을 업데이트
 * 각 작업의 종료 시간에서 요청 시간을 뺀 값을 누적하여 합계를 계산
 * 모든 작업을 처리한 후, 누적 합계를 작업 수로 나누어 평균을 계산
*/

public class Ex2_1 {
    public static void main(String[] args) {
        Ex2_1 ex2 = new Ex2_1();

        int[][] jobs1 = {{0, 3}, {1, 9}, {2, 6}};

        // 9 반환
        System.out.println(ex2.solution(jobs1));
    }

    public int solution(int[][] jobs) {
        // 작업의 소요 시간을 기준으로 정렬하는 우선순위 큐를 생성
        PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> a[1] - b[1]);

        // 작업을 요청 시간 순으로 정렬
//        Arrays.sort(jobs, Comparator.comparingInt(a -> a[0]));
        Arrays.sort(jobs, (a, b) -> a[0] - b[0]);

        // 처리된 작업의 수
        int count = 0;
        // time: 현재 시간
        int time = 0;
        // jobs 배열의 현재 인덱스
        int idx = 0;
        // 작업의 요청부터 종료까지 걸린 시간의 합계
        int answer = 0;

        // 모든 작업이 처리될 때까지 반복
        while (count < jobs.length) {
            // 현재 시간 이전에 요청된 모든 작업을 큐에 추가
            while (idx < jobs.length && jobs[idx][0] <= time) {
                queue.offer(jobs[idx++]);
            }

            // 큐가 비어 있으면 다음 작업의 요청 시간으로 현재 시간을 업데이트
            if (queue.isEmpty()) {
                time = jobs[idx][0];
            } else {
                // 큐가 비어 있지 않으면 가장 짧은 작업을 처리
                int[] job = queue.poll();

                // 작업의 소요 시간을 현재 시간에 더함
                time += job[1];

                // 작업의 종료 시간에서 요청 시간을 뺀 값을 누적하여 합계를 계산
                answer += time - job[0];

                // 처리된 작업 수를 증가
                count++;
            }
        }

        // 모든 작업을 처리한 후, 누적 합계를 작업 수로 나누어 평균을 계산하고 반환
        return (int)Math.floor(answer / count);
    }
}