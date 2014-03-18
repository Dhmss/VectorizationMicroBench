	.file	"polyEval.c"
	.text
	.p2align 4,,15
.globl Java_polynomialComputation_polyEval_registerNatives
	.type	Java_polynomialComputation_polyEval_registerNatives, @function
Java_polynomialComputation_polyEval_registerNatives:
.LFB798:
	.cfi_startproc
	movq	(%rdi), %rax
	movl	$3, %ecx
	movq	JNI_METHODS@GOTPCREL(%rip), %rdx
	movq	1720(%rax), %rax
	jmp	*%rax
	.cfi_endproc
.LFE798:
	.size	Java_polynomialComputation_polyEval_registerNatives, .-Java_polynomialComputation_polyEval_registerNatives
	.p2align 4,,15
.globl jni_basic
	.type	jni_basic, @function
jni_basic:
.LFB799:
	.cfi_startproc
	pushq	%r15
	.cfi_def_cfa_offset 16
	.cfi_offset 15, -16
	pushq	%r14
	.cfi_def_cfa_offset 24
	.cfi_offset 14, -24
	movl	%r9d, %r14d
	pushq	%r13
	.cfi_def_cfa_offset 32
	.cfi_offset 13, -32
	pushq	%r12
	.cfi_def_cfa_offset 40
	.cfi_offset 12, -40
	pushq	%rbp
	.cfi_def_cfa_offset 48
	.cfi_offset 6, -48
	pushq	%rbx
	.cfi_def_cfa_offset 56
	.cfi_offset 3, -56
	movq	%rdi, %rbx
	subq	$72, %rsp
	.cfi_def_cfa_offset 128
	movq	(%rdi), %rax
	movq	%rdx, 32(%rsp)
	movq	%rcx, 40(%rsp)
	xorl	%edx, %edx
	movq	%r8, 48(%rsp)
	movl	128(%rsp), %r12d
	movq	32(%rsp), %rsi
	call	*1776(%rax)
	movq	%rax, 24(%rsp)
	movq	(%rbx), %rax
	xorl	%edx, %edx
	movq	40(%rsp), %rsi
	movq	%rbx, %rdi
	call	*1776(%rax)
	movq	%rax, %rbp
	movq	(%rbx), %rax
	xorl	%edx, %edx
	movq	48(%rsp), %rsi
	movq	%rbx, %rdi
	call	*1776(%rax)
	testl	%r12d, %r12d
	movq	%rax, %r13
	jle	.L4
	movq	24(%rsp), %rdx
	movslq	%r14d, %rax
	leaq	(%rdx,%rax,8), %rsi
	leal	-1(%r12), %edx
	movq	%r13, %rax
	leaq	8(%r13,%rdx,8), %rcx
	.p2align 4,,10
	.p2align 3
.L5:
	movq	(%rsi), %rdx
	movq	%rdx, (%rax)
	addq	$8, %rax
	cmpq	%rcx, %rax
	jne	.L5
.L4:
	subl	$1, %r14d
	js	.L6
	movq	24(%rsp), %rsi
	movslq	%r14d, %r10
	mov	%r14d, %eax
	salq	$3, %r10
	salq	$3, %rax
	movl	%r12d, %ecx
	shrl	$2, %ecx
	movq	%r13, %r11
	leaq	32(%rbp), %r14
	addq	%r10, %rsi
	subq	$8, %r10
	leal	0(,%rcx,4), %r8d
	subq	%rax, %r10
	leal	-1(%r12), %eax
	addq	24(%rsp), %r10
	andl	$15, %r11d
	leaq	8(,%rax,8), %r9
	leaq	32(%r13), %rax
	movq	%rax, 56(%rsp)
	.p2align 4,,10
	.p2align 3
.L15:
	vmovsd	(%rsi), %xmm0
	testl	%r12d, %r12d
	vcvttsd2si	%xmm0, %edi
	jle	.L7
	cmpl	$6, %r12d
	jbe	.L8
	testq	%r11, %r11
	jne	.L8
	cmpq	%r14, %r13
	jbe	.L24
.L17:
	testl	%r8d, %r8d
	.p2align 4,,2
	je	.L25
	movl	%edi, 12(%rsp)
	movq	%r13, %rax
	movq	%rbp, %rdx
	vmovd	12(%rsp), %xmm0
	xorl	%r15d, %r15d
	vpshufd	$0, %xmm0, %xmm4
	vpshufd	$238, %xmm4, %xmm3
	vcvtdq2pd	%xmm4, %xmm4
	vcvtdq2pd	%xmm3, %xmm3
	.p2align 4,,10
	.p2align 3
.L12:
	vmovupd	16(%rax), %xmm1
	addl	$1, %r15d
	vmovupd	16(%rdx), %xmm0
	vmovupd	(%rax), %xmm2
	vmulpd	%xmm0, %xmm1, %xmm0
	vmovupd	(%rdx), %xmm1
	addq	$32, %rdx
	vmulpd	%xmm1, %xmm2, %xmm1
	vaddpd	%xmm3, %xmm0, %xmm0
	vaddpd	%xmm4, %xmm1, %xmm1
	vmovapd	%xmm0, 16(%rax)
	vmovapd	%xmm1, (%rax)
	addq	$32, %rax
	cmpl	%ecx, %r15d
	jb	.L12
	cmpl	%r8d, %r12d
	movl	%r8d, %edx
	je	.L7
.L11:
	vcvtsi2sd	%edi, %xmm1, %xmm1
	movslq	%edx, %r15
	salq	$3, %r15
	leaq	0(%r13,%r15), %rax
	leaq	0(%rbp,%r15), %r15
	.p2align 4,,10
	.p2align 3
.L13:
	vmovsd	(%rax), %xmm0
	addl	$1, %edx
	vmulsd	(%r15), %xmm0, %xmm0
	addq	$8, %r15
	vaddsd	%xmm1, %xmm0, %xmm0
	vmovsd	%xmm0, (%rax)
	addq	$8, %rax
	cmpl	%edx, %r12d
	jg	.L13
.L7:
	subq	$8, %rsi
	cmpq	%r10, %rsi
	jne	.L15
.L6:
	movq	(%rbx), %rax
	movq	24(%rsp), %rdx
	movq	%rbx, %rdi
	movq	32(%rsp), %rsi
	xorl	%ecx, %ecx
	call	*1784(%rax)
	movq	(%rbx), %rax
	movq	%rbp, %rdx
	movq	40(%rsp), %rsi
	movq	%rbx, %rdi
	xorl	%ecx, %ecx
	call	*1784(%rax)
	movq	(%rbx), %rax
	movq	48(%rsp), %rsi
	movq	%rbx, %rdi
	movq	%r13, %rdx
	xorl	%ecx, %ecx
	movq	1784(%rax), %rax
	addq	$72, %rsp
	.cfi_remember_state
	.cfi_def_cfa_offset 56
	popq	%rbx
	.cfi_def_cfa_offset 48
	popq	%rbp
	.cfi_def_cfa_offset 40
	popq	%r12
	.cfi_def_cfa_offset 32
	popq	%r13
	.cfi_def_cfa_offset 24
	popq	%r14
	.cfi_def_cfa_offset 16
	popq	%r15
	.cfi_def_cfa_offset 8
	jmp	*%rax
	.p2align 4,,10
	.p2align 3
.L24:
	.cfi_restore_state
	cmpq	56(%rsp), %rbp
	ja	.L17
	.p2align 4,,10
	.p2align 3
.L8:
	vcvtsi2sd	%edi, %xmm1, %xmm1
	xorl	%eax, %eax
	.p2align 4,,10
	.p2align 3
.L14:
	vmovsd	0(%r13,%rax), %xmm0
	vmulsd	0(%rbp,%rax), %xmm0, %xmm0
	vaddsd	%xmm1, %xmm0, %xmm0
	vmovsd	%xmm0, 0(%r13,%rax)
	addq	$8, %rax
	cmpq	%r9, %rax
	jne	.L14
	subq	$8, %rsi
	cmpq	%r10, %rsi
	jne	.L15
	jmp	.L6
.L25:
	xorl	%edx, %edx
	.p2align 4,,2
	jmp	.L11
	.cfi_endproc
.LFE799:
	.size	jni_basic, .-jni_basic
	.p2align 4,,15
.globl jni_avxa
	.type	jni_avxa, @function
jni_avxa:
.LFB801:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	pushq	%r15
	pushq	%r14
	movl	%r9d, %r14d
	.cfi_offset 14, -32
	.cfi_offset 15, -24
	pushq	%r13
	pushq	%r12
	pushq	%rbx
	movq	%rdi, %rbx
	.cfi_offset 3, -56
	.cfi_offset 12, -48
	.cfi_offset 13, -40
	subq	$104, %rsp
	movq	(%rdi), %rax
	movl	16(%rbp), %r13d
	movq	%rcx, 56(%rsp)
	movq	%rdx, 64(%rsp)
	xorl	%edx, %edx
	movq	%r8, 48(%rsp)
	movq	64(%rsp), %rsi
	call	*1776(%rax)
	movq	%rax, 72(%rsp)
	movq	(%rbx), %rax
	xorl	%edx, %edx
	movq	56(%rsp), %rsi
	movq	%rbx, %rdi
	call	*1776(%rax)
	movq	%rax, %r12
	movq	(%rbx), %rax
	xorl	%edx, %edx
	movq	48(%rsp), %rsi
	movq	%rbx, %rdi
	call	*1776(%rax)
	leaq	48(%r12), %rdx
	leaq	48(%rax), %rcx
	testl	%r13d, %r13d
	movq	%rdx, 32(%rsp)
	movq	%rcx, 40(%rsp)
	jle	.L27
	movq	72(%rsp), %rcx
	movslq	%r14d, %rdx
	leaq	(%rcx,%rdx,8), %rdi
	leal	-1(%r13), %ecx
	movq	%rax, %rdx
	leaq	8(%rax,%rcx,8), %rsi
	.p2align 4,,10
	.p2align 3
.L28:
	movq	(%rdi), %rcx
	movq	%rcx, 48(%rdx)
	addq	$8, %rdx
	cmpq	%rsi, %rdx
	jne	.L28
.L27:
	movl	%r14d, %r9d
	subl	$1, %r9d
	js	.L29
	movq	72(%rsp), %rcx
	movslq	%r9d, %rdx
	leaq	88(%rsp), %r8
	leaq	56(%rax), %r15
	leal	-1(%r13), %r14d
	movq	%rbx, 24(%rsp)
	leaq	(%rcx,%rdx,8), %r10
	leal	-5(%r13), %edx
	shrl	$2, %edx
	mov	%edx, %esi
	leal	4(,%rdx,4), %r11d
	addq	$1, %rsi
	salq	$5, %rsi
	.p2align 4,,10
	.p2align 3
.L35:
	movq	(%r10), %rdx
	cmpl	$4, %r13d
	movq	%rdx, 88(%rsp)
	vbroadcastsd	(%r8), %ymm1
	movl	$0, 12(%rsp)
	jle	.L31
	xorl	%edx, %edx
	.p2align 4,,10
	.p2align 3
.L32:
	vmovapd	48(%rax,%rdx), %ymm0
	vmulpd	48(%r12,%rdx), %ymm0, %ymm0
	vaddpd	%ymm1, %ymm0, %ymm0
	vmovapd	%ymm0, 48(%rax,%rdx)
	addq	$32, %rdx
	cmpq	%rsi, %rdx
	jne	.L32
	movl	%r11d, 12(%rsp)
.L31:
	cmpl	12(%rsp), %r13d
	jle	.L33
	movslq	12(%rsp), %rdi
	movl	%r14d, %ebx
	subl	12(%rsp), %ebx
	leaq	48(,%rdi,8), %rcx
	movq	%rbx, (%rsp)
	leaq	(%rdi,%rbx), %rbx
	leaq	(%rax,%rcx), %rdx
	leaq	(%r15,%rbx,8), %rbx
	leaq	(%r12,%rcx), %rcx
	.p2align 4,,10
	.p2align 3
.L34:
	vmovsd	(%rdx), %xmm0
	vmulsd	(%rcx), %xmm0, %xmm0
	addq	$8, %rcx
	vmovsd	%xmm0, (%rdx)
	vaddsd	88(%rsp), %xmm0, %xmm0
	vmovsd	%xmm0, (%rdx)
	addq	$8, %rdx
	cmpq	%rbx, %rdx
	jne	.L34
.L33:
	subq	$8, %r10
	subl	$1, %r9d
	jns	.L35
	movq	24(%rsp), %rbx
.L29:
	movq	(%rbx), %rax
	movq	72(%rsp), %rdx
	movq	%rbx, %rdi
	movq	64(%rsp), %rsi
	xorl	%ecx, %ecx
	call	*1784(%rax)
	movq	(%rbx), %rax
	movq	32(%rsp), %rdx
	movq	%rbx, %rdi
	movq	56(%rsp), %rsi
	xorl	%ecx, %ecx
	call	*1784(%rax)
	movq	(%rbx), %rax
	movq	40(%rsp), %rdx
	movq	%rbx, %rdi
	movq	48(%rsp), %rsi
	xorl	%ecx, %ecx
	call	*1784(%rax)
	addq	$104, %rsp
	popq	%rbx
	popq	%r12
	popq	%r13
	popq	%r14
	popq	%r15
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE801:
	.size	jni_avxa, .-jni_avxa
	.p2align 4,,15
.globl jni_avxu
	.type	jni_avxu, @function
jni_avxu:
.LFB800:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	pushq	%r15
	movl	%r9d, %r15d
	.cfi_offset 15, -24
	pushq	%r14
	pushq	%r13
	pushq	%r12
	pushq	%rbx
	movq	%rdi, %rbx
	.cfi_offset 3, -56
	.cfi_offset 12, -48
	.cfi_offset 13, -40
	.cfi_offset 14, -32
	subq	$120, %rsp
	movq	(%rdi), %rax
	movl	16(%rbp), %r14d
	movq	%rcx, 56(%rsp)
	movq	%r8, 48(%rsp)
	movq	%rdx, 64(%rsp)
	xorl	%edx, %edx
	movq	64(%rsp), %rsi
	call	*1776(%rax)
	movq	%rax, 72(%rsp)
	movq	(%rbx), %rax
	xorl	%edx, %edx
	movq	56(%rsp), %rsi
	movq	%rbx, %rdi
	call	*1776(%rax)
	movq	%rax, %r13
	movq	(%rbx), %rax
	xorl	%edx, %edx
	movq	48(%rsp), %rsi
	movq	%rbx, %rdi
	call	*1776(%rax)
	movq	%rax, %r12
	leaq	80(%rsp), %rax
	xorl	%esi, %esi
	movq	%rax, %rdi
	movq	%rax, 40(%rsp)
	call	gettimeofday@PLT
	movq	80(%rsp), %rdx
	movq	88(%rsp), %rax
	testl	%r14d, %r14d
	movq	%rdx, 24(%rsp)
	movq	%rax, 32(%rsp)
	jle	.L42
	movq	72(%rsp), %rdx
	movslq	%r15d, %rax
	leaq	(%rdx,%rax,8), %rsi
	leal	-1(%r14), %edx
	movq	%r12, %rax
	leaq	8(%r12,%rdx,8), %rcx
	.p2align 4,,10
	.p2align 3
.L43:
	movq	(%rsi), %rdx
	movq	%rdx, (%rax)
	addq	$8, %rax
	cmpq	%rcx, %rax
	jne	.L43
.L42:
	movl	%r15d, %r9d
	leal	-4(%r14), %ecx
	subl	$1, %r9d
	js	.L44
	movq	72(%rsp), %rdx
	movslq	%r9d, %rax
	leaq	104(%rsp), %rdi
	leaq	8(%r12), %r15
	leal	-1(%r14), %r11d
	movq	%rbx, 16(%rsp)
	leaq	(%rdx,%rax,8), %r8
	leal	-5(%r14), %eax
	shrl	$2, %eax
	leal	4(,%rax,4), %r10d
	.p2align 4,,10
	.p2align 3
.L50:
	movq	(%r8), %rax
	xorl	%edx, %edx
	movq	%rax, 104(%rsp)
	xorl	%eax, %eax
	testl	%ecx, %ecx
	vbroadcastsd	(%rdi), %ymm2
	movl	$0, 12(%rsp)
	jle	.L46
	.p2align 4,,10
	.p2align 3
.L52:
	leaq	(%r12,%rax), %rbx
	vmovupd	0(%r13,%rax), %ymm0
	addl	$4, %edx
	addq	$32, %rax
	cmpl	%edx, %ecx
	vmovupd	(%rbx), %ymm1
	vmulpd	%ymm0, %ymm1, %ymm0
	vaddpd	%ymm2, %ymm0, %ymm0
	vmovupd	%ymm0, (%rbx)
	jg	.L52
	movl	%r10d, 12(%rsp)
.L46:
	cmpl	12(%rsp), %r14d
	jle	.L48
	movslq	12(%rsp), %rsi
	movl	%r11d, %ebx
	subl	12(%rsp), %ebx
	leaq	0(,%rsi,8), %rdx
	movq	%rbx, (%rsp)
	leaq	(%rsi,%rbx), %rbx
	leaq	(%r12,%rdx), %rax
	leaq	(%r15,%rbx,8), %rbx
	leaq	0(%r13,%rdx), %rdx
	.p2align 4,,10
	.p2align 3
.L49:
	vmovsd	(%rax), %xmm0
	vmulsd	(%rdx), %xmm0, %xmm0
	addq	$8, %rdx
	vmovsd	%xmm0, (%rax)
	vaddsd	104(%rsp), %xmm0, %xmm0
	vmovsd	%xmm0, (%rax)
	addq	$8, %rax
	cmpq	%rbx, %rax
	jne	.L49
.L48:
	subq	$8, %r8
	subl	$1, %r9d
	jns	.L50
	movq	16(%rsp), %rbx
.L44:
	movq	40(%rsp), %rdi
	xorl	%esi, %esi
	call	gettimeofday@PLT
	movq	80(%rsp), %r9
	movq	(%rbx), %rax
	movq	%rbx, %rdi
	movq	72(%rsp), %rdx
	movq	64(%rsp), %rsi
	xorl	%ecx, %ecx
	imulq	$1000000, %r9, %r14
	subq	32(%rsp), %r14
	addq	88(%rsp), %r14
	call	*1784(%rax)
	movq	(%rbx), %rax
	movq	%r13, %rdx
	movq	56(%rsp), %rsi
	movq	%rbx, %rdi
	xorl	%ecx, %ecx
	call	*1784(%rax)
	movq	(%rbx), %rax
	movq	%r12, %rdx
	movq	48(%rsp), %rsi
	movq	%rbx, %rdi
	xorl	%ecx, %ecx
	call	*1784(%rax)
	movq	24(%rsp), %r8
	addq	$120, %rsp
	popq	%rbx
	popq	%r12
	imulq	$1000000, %r8, %rax
	popq	%r13
	subq	%rax, %r14
	movq	%r14, %rax
	popq	%r14
	popq	%r15
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE800:
	.size	jni_avxu, .-jni_avxu
.globl JNI_METHODS
	.section	.rodata.str1.1,"aMS",@progbits,1
.LC0:
	.string	"jni_avxu"
.LC1:
	.string	"([D[D[DII)J"
.LC2:
	.string	"jni_basic"
.LC3:
	.string	"([D[D[DII)V"
.LC4:
	.string	"jni_avxa"
	.section	.data.rel.ro,"aw",@progbits
	.align 32
	.type	JNI_METHODS, @object
	.size	JNI_METHODS, 72
JNI_METHODS:
	.quad	.LC0
	.quad	.LC1
	.quad	jni_avxu
	.quad	.LC2
	.quad	.LC3
	.quad	jni_basic
	.quad	.LC4
	.quad	.LC3
	.quad	jni_avxa
.globl NB_JNI_METHODS
	.section	.rodata
	.align 4
	.type	NB_JNI_METHODS, @object
	.size	NB_JNI_METHODS, 4
NB_JNI_METHODS:
	.long	3
	.ident	"GCC: (GNU) 4.4.7 20120313 (Red Hat 4.4.7-4)"
	.section	.note.GNU-stack,"",@progbits
