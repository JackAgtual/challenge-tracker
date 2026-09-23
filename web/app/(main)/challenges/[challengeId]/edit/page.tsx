import ChallengeForm from "@/components/ChallengeForm";
import { Button } from "@/components/ui/button";
import { client } from "@/lib/api-client";
import { TCreateChallengeFormSchema } from "@/types/form-types";
import Link from "next/link";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: number }>;
}) {
  const { challengeId } = await params;

  const res = await client.GET("/challenges/{challengeId}", {
    params: { path: { challengeId } },
  });

  const defaultVals: TCreateChallengeFormSchema = res.error
    ? { name: "", durationDays: 0 }
    : {
        name: res.data.challenge.name,
        durationDays: res.data.challenge.durationDays || 0,
      };

  return (
    <div>
      <h1>Edit challenge</h1>
      <ChallengeForm
        action="EDIT"
        challengeId={challengeId}
        defaultValues={defaultVals}
      />
      <Link href={`/challenges/${challengeId}`}>
        <Button variant="outline">Cancel</Button>
      </Link>
    </div>
  );
}
