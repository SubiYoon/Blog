import Swal from "sweetalert2";



/**
 * SweetAlert2 confirm 사용법
 * @param title 제목
 * @param html html태그로된 내용
 * @param icon 아이콘 (success, error, warning, info, question)
 * @param okText 확인버튼 문구
 * @param cancelText 취소버튼 문구
 * @returns {Promise<boolean>} confirm ui
 */
export async function $confirm(title, html, icon, okText, cancelText,) {
    let isConfirm = false;

    await Swal.fire({
        title: title,
        html: html,
        icon: icon || 'question',

        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
        confirmButtonText: okText || '확인', // confirm 버튼 텍스트 지정
        cancelButtonText: cancelText || '취소', // cancel 버튼 텍스트 지정

        reverseButtons: true, // 버튼 순서 거꾸로

    }).then(result => {
        // 만약 Promise리턴을 받으면,
        // if (result.isConfirmed) { // 만약 모달창에서 confirm 버튼을 눌렀다면
        //     return true;
        // } else {
        //     return false;
        // }
        isConfirm = result.isConfirmed;
    });

    return isConfirm;
}

/**
 * SweetAlert2 prompt 사용법
 * @param title 제목
 * @param html html태그로된 내용
 * @param inputType input type (text, email, number)
 * @param placeholder placeholder
 * @returns {Promise<string>} prompt ui
 */
export async function $prompt(title, html, inputType, placeholder) {
    const { value: getName } = await Swal.fire({
        title: title,
        html: html,
        input: inputType || 'text',
        inputPlaceholder: placeholder || '입력해주세요.'
    })

    return getName;
}

/**
 * SweetAlert2 alert 사용법
 * @param title 제목
 * @param html html태그로된 내용
 * @param icon 아이콘 (success, error, warning, info, question)
 */
export async function $alert(title, html, icon) {
    await Swal.fire({
        title: title,
        html: html,
        icon: icon || 'success'
    });
}
