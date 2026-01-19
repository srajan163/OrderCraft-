import { Directive, ElementRef, Input, OnChanges } from '@angular/core';

@Directive({
  selector: '[gradient]'
})
export class GradientDirective implements OnChanges {
  @Input() gradient: string[] = [];
  @Input() rotation: number = 0;

  constructor(private el: ElementRef) {}

  ngOnChanges() {
    this.el.nativeElement.style.background =
      `linear-gradient(${this.rotation}deg, ${this.gradient.join(', ')})`;

    this.el.nativeElement.style.transition = "background 1.2s ease";
  }
}
