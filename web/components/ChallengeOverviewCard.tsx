import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { components } from "@/types/api";

type ChallengeOverviewCardProps = {
  challenge: components["schemas"]["ChallengeResponse"];
};

export default function ChallengeOverviewCard({
  challenge,
}: ChallengeOverviewCardProps) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>{challenge.name}</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="grid grid-cols-2">
          <p>Status</p>
          <p>{challenge.status}</p>
          <p>Duration (days)</p>
          <p>{challenge.durationDays || "Not set"}</p>
          <p>Start date</p>
          <p>{challenge.starDate || "Not set"}</p>
        </div>
      </CardContent>
    </Card>
  );
}
