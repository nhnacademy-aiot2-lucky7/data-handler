package com.nhnacademy.sensor;

/**
 * 센서의 상태를 나타내는 열거형(enum)입니다. <br>
 * 센서와 데이터 타입 간 매핑의 현재 상태를 구분하는 데 사용됩니다.
 *
 * <ul>
 *     <li>{@link #PENDING} - 매핑이 생성되었지만 아직 처리되지 않은 상태</li>
 *     <li>{@link #COMPLETED} - 매핑이 정상적으로 완료된 상태</li>
 *     <li>{@link #ABANDONED} - 매핑이 폐기되거나 무시된 상태</li>
 * </ul>
 */
public enum SensorStatus {

    /**
     * <b><p>Default</p></b>
     * 매핑이 생성되었지만 아직 완료되지 않은 상태입니다.
     */
    PENDING,

    /**
     * 매핑이 정상적으로 완료된 상태입니다.
     */
    COMPLETED,

    /**
     * 매핑이 폐기되었거나 더 이상 사용되지 않는 상태입니다.
     */
    ABANDONED
}
