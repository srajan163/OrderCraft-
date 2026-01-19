import { Injectable } from '@angular/core';

export interface GradientPreset {
  name: string;
  colors: string[];
  angle?: number;
}

@Injectable({ providedIn: 'root' })
export class GradientService {
  rotations = [0, 30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330];

  gradients: GradientPreset[] = [
    { name: 'Telegram', colors: ['#1c92d2', '#f2fcfe'], angle: 0 },
    { name: 'Sunrise', colors: ['#ff7e5f', '#feb47b'], angle: 45 },
    { name: 'Ocean', colors: ['#2b5876', '#4e4376'], angle: 120 },
    { name: 'Aurora', colors: ['#00c6ff', '#0072ff', '#7bff6f'], angle: 200 },
    { name: 'Violet', colors: ['#6a11cb', '#2575fc'], angle: 140 },
  ];

  randomPreset() {
    const i = Math.floor(Math.random() * this.gradients.length);
    return this.gradients[i];
  }
}
