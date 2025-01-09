import { Component, OnInit } from '@angular/core';
import { CandidateService } from '../../services/candidate.service';

interface PositionStat {
  name: string;
  count: number;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.page.html',
  styleUrls: ['./dashboard.page.scss']
})
export class DashboardPage implements OnInit {
  totalCandidates: number = 0;
  positionStats: PositionStat[] = [];

  constructor(private candidateService: CandidateService) {}

  ngOnInit() {
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.candidateService.getDashboardStats().subscribe(
      stats => {
        this.totalCandidates = stats.totalCandidates;
        this.positionStats = stats.positionStats;
      },
      error => {
        console.error('Error loading dashboard data:', error);
      }
    );
  }
} 